// Ejecuta pruebas sin instalar dependencias: npm test o node tests/run.js.
const fs=require('fs'),path=require('path'),vm=require('vm'),assert=require('assert'),cp=require('child_process'),os=require('os');
const root=path.join(__dirname,'..'); let ok=0;
function test(nombre,fn){try{fn();ok++;console.log('OK '+nombre);}catch(e){console.error('FALLA '+nombre+' '+e.stack);process.exitCode=1;}}
function walk(dir){return fs.readdirSync(dir,{withFileTypes:true}).flatMap(e=>e.isDirectory()?walk(path.join(dir,e.name)):[path.join(dir,e.name)]);}
const files=walk(path.join(root,'public'));
const tmp=fs.mkdtempSync(path.join(os.tmpdir(),'interfaces-test-'));
try {
 for(const file of files.filter(f=>f.endsWith('.html'))){
 const h=fs.readFileSync(file,'utf8');
 test('Enlaces '+path.basename(file),()=>{for(const m of h.matchAll(/(?:href|src)="(\/[^"#]+)"/g))assert(fs.existsSync(path.join(root,'public',m[1])),m[1]);});
 test('JS '+path.basename(file),()=>{let i=0;for(const m of h.matchAll(/<script([^>]*)>([\s\S]*?)<\/script>/g)){if(!m[2].trim())continue;const f=path.join(tmp,'script'+i+++(m[1].includes('module')?'.mjs':'.js'));fs.writeFileSync(f,m[2]);cp.execFileSync(process.execPath,['--check',f],{stdio:'pipe'});}});
 if(file.includes(path.sep+'evaluables'+path.sep)||file.includes(path.sep+'profesor'+path.sep)&&!file.endsWith('index.html'))
 test('Enunciado completo '+path.basename(file),()=>{const e=h.match(/<section class="caja enunciado">([\s\S]*?)<\/section>/);assert(e);assert((e[1].match(/<li>/g)||[]).length>=6);assert(e[1].includes('Comprobacion final'));assert(!e[1].includes('<erp-'));});
 }
 for(const f of files.filter(f=>f.endsWith('.js'))){test('Sintaxis '+path.basename(f),()=>{const out=path.join(tmp,'mod.mjs');fs.writeFileSync(out,fs.readFileSync(f));cp.execFileSync(process.execPath,['--check',out],{stdio:'pipe'});});}
 const L=require('../public/js/logica.js');
 test('Normaliza voz',()=>assert.equal(L.normalizar(' GUARDAR. '),'guardar'));
 test('Indice avanza',()=>assert.equal(L.siguiente(2,1,3),0));
 test('Indice retrocede',()=>assert.equal(L.siguiente(0,-1,3),2));
 test('Indice lista vacia',()=>assert.equal(L.siguiente(0,1,0),0));
 const productos=[{name:'Teclado',stock:3},{name:'Monitor',stock:40}];
 test('Stock bajo',()=>assert.deepEqual(L.filtrar(productos,'stock bajo'),[productos[0]]));
 test('Buscar sin distinguir mayusculas',()=>assert.equal(L.filtrar(productos,'BUSCAR TECLADO').length,1));
 test('Sin coincidencias',()=>assert.equal(L.filtrar(productos,'buscar xyz').length,0));
 test('Todos',()=>assert.equal(L.filtrar(productos,'todos').length,2));
 test('Movimiento sin foto previa',()=>assert.deepEqual(L.movimiento(new Uint8Array(8),null,2),{izq:0,der:0}));
 test('Movimiento izquierda',()=>assert.deepEqual(L.movimiento([100,100,100,255,0,0,0,255],[0,0,0,255,0,0,0,255],2),{izq:1,der:0}));
 test('Sin movimiento',()=>assert.deepEqual(L.movimiento([0,0,0,255],[0,0,0,255],1),{izq:0,der:0}));
 test('Pinza cerrada',()=>{const m=Array.from({length:21},()=>({x:0,y:0}));assert(L.pinza(m));m[8].x=1;assert(!L.pinza(m));assert(!L.pinza(null));});
 // Este DOM minimo prueba contratos, NO calcula layout ni simula una camara real.
 class El {constructor(){this.a={};this.l={};this.shadowRoot=null;}attachShadow(){return this.shadowRoot={innerHTML:''};}getAttribute(k){return this.a[k]??null;}setAttribute(k,v){this.a[k]=String(v);if(this.constructor.observedAttributes?.includes(k))this.attributeChangedCallback();}addEventListener(k,f){(this.l[k]??=[]).push(f);}dispatchEvent(e){(this.l[e.type]||[]).forEach(f=>f(e));}click(){this.dispatchEvent({type:'click'});}}
 const reg={};const ctx=vm.createContext({HTMLElement:El,CustomEvent:class{constructor(t,o){this.type=t;Object.assign(this,o);}},customElements:{define:(n,c)=>reg[n]=c}});
 vm.runInContext(fs.readFileSync(path.join(root,'public/componentes/erp-tarjeta.js'),'utf8'),ctx);
 test('Tarjeta valores por defecto',()=>{const t=new reg['erp-tarjeta']();assert.equal(t.titulo,'Sin titulo');assert.equal(t.valor,'0');});
 test('Tarjeta actualiza atributo y texto',()=>{const t=new reg['erp-tarjeta']();t.valor=12;assert.equal(t.getAttribute('valor'),'12');assert(t.shadowRoot.innerHTML.includes('12'));});
 test('Tarjeta evita HTML de usuario',()=>{const t=new reg['erp-tarjeta']();t.titulo='<img src=x>';assert(!t.shadowRoot.innerHTML.includes('<img'));});
 test('Tarjeta un evento tras reconectar',()=>{const t=new reg['erp-tarjeta']();t.connectedCallback();t.connectedCallback();let n=0;t.addEventListener('seleccionada',()=>n++);t.click();assert.equal(n,1);});
 const prof=path.join(root,'public/profesor/componentes/erp-insignia.js');
 if(fs.existsSync(prof)){
 vm.runInContext(fs.readFileSync(prof,'utf8'),ctx);
 test('Insignia defecto',()=>assert.equal(new reg['erp-insignia']().estado,'pending'));
 test('Insignia invalida',()=>{const t=new reg['erp-insignia']();t.estado='xyz';assert.equal(t.estado,'pending');});
 test('Insignia ciclo y evento',()=>{const t=new reg['erp-insignia']();let n=0;t.addEventListener('cambio',e=>{assert(e.detail.estado);n++;});for(let i=0;i<4;i++)t.click();assert.equal(t.estado,'pending');assert.equal(n,4);});
 test('Insignia sin eventos duplicados',()=>{const t=new reg['erp-insignia']();t.connectedCallback();t.connectedCallback();let n=0;t.addEventListener('cambio',()=>n++);t.click();assert.equal(n,1);assert.equal(t.estado,'paid');});
 }
 test('Enunciados sin alturas fijas ni recorte',()=>{const c=fs.readFileSync(path.join(root,'public/css/estilos.css'),'utf8');assert(c.includes('height:auto;max-height:none;overflow:visible'));assert(c.includes('overflow-wrap:anywhere'));});
 test('Inicio apunta a index.html',()=>assert(require('../next.config.js').rewrites));
} finally {fs.rmSync(tmp,{recursive:true,force:true});}
console.log('TOTAL '+ok+' pruebas aprobadas. '+(process.exitCode?'HAY FALLOS':'Sin fallos.'));
