// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa android.Manifest para poder utilizar su API.
import android.Manifest
// Importa android.annotation.SuppressLint para poder utilizar su API.
import android.annotation.SuppressLint
// Importa android.app.Activity para poder utilizar su API.
import android.app.Activity
// Importa android.app.AlertDialog para poder utilizar su API.
import android.app.AlertDialog
// Importa android.bluetooth.BluetoothManager para poder utilizar su API.
import android.bluetooth.BluetoothManager
// Importa android.content.* para poder utilizar su API.
import android.content.*
// Importa android.content.pm.PackageManager para poder utilizar su API.
import android.content.pm.PackageManager
// Importa android.graphics.Bitmap para poder utilizar su API.
import android.graphics.Bitmap
// Importa android.graphics.BitmapFactory para poder utilizar su API.
import android.graphics.BitmapFactory
// Importa android.graphics.Color para poder utilizar su API.
import android.graphics.Color
// Importa android.hardware.* para poder utilizar su API.
import android.hardware.*
// Importa android.media.MediaPlayer para poder utilizar su API.
import android.media.MediaPlayer
// Importa android.media.MediaRecorder para poder utilizar su API.
import android.media.MediaRecorder
// Importa android.net.Uri para poder utilizar su API.
import android.net.Uri
// Importa android.os.Build para poder utilizar su API.
import android.os.Build
// Importa android.os.Bundle para poder utilizar su API.
import android.os.Bundle
// Importa android.provider.MediaStore para poder utilizar su API.
import android.provider.MediaStore
// Importa android.text.Editable para poder utilizar su API.
import android.text.Editable
// Importa android.text.TextWatcher para poder utilizar su API.
import android.text.TextWatcher
// Importa android.view.WindowInsets para poder utilizar su API.
import android.view.WindowInsets
// Importa android.widget.* para poder utilizar su API.
import android.widget.*
// Importa org.json.JSONArray para poder utilizar su API.
import org.json.JSONArray
// Importa org.json.JSONObject para poder utilizar su API.
import org.json.JSONObject
// Importa java.io.File para poder utilizar su API.
import java.io.File
// Importa java.net.URL para poder utilizar su API.
import java.net.URL
// Importa java.util.concurrent.Executors para poder utilizar su API.
import java.util.concurrent.Executors
// Importa javax.net.ssl.HttpsURLConnection para poder utilizar su API.
import javax.net.ssl.HttpsURLConnection

// Declara el modelo o componente que agrupa estas responsabilidades.
class MainActivity : Activity(), SensorEventListener {
 // Paso del flujo: private lateinit var box: LinearLayout
 private lateinit var box: LinearLayout
 // Paso del flujo: private lateinit var status: TextView
 private lateinit var status: TextView
 // Paso del flujo: private lateinit var store: Store
 private lateinit var store: Store
 // Paso del flujo: private lateinit var sensors: SensorManager
 private lateinit var sensors: SensorManager
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 private val worker=Executors.newSingleThreadExecutor()
 // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
 private val link=BluetoothLink()
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var selected: Player?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var image: Bitmap?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var original: Bitmap?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var picture: ImageView?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var audio: MediaPlayer?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var prepared=false
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var recorder: MediaRecorder?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var recordingFile: File?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var query=""
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var club=""
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var position=""
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var onlyFavorites=false
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var advanced=false
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var descending=false
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var shake=false
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var lastShake=0L
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var visible=emptyList<Player>()
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var refresh: (() -> Unit)?=null
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var audioFiles=emptyList<File>()
 // Reserva un estado que podrá actualizarse al producirse eventos.
 private var audioIndex=0
 // Guarda el último resultado de comprobar cada ejercicio (1..16) para pintarlo en Academia.
 private val checkStates=mutableMapOf<Int,CheckState>()
 // Implementa la respuesta al evento onCreate.
 override fun onCreate(savedInstanceState: Bundle?) {
  // Mantiene el comportamiento heredado de Android en este evento.
  super.onCreate(savedInstanceState)
  // Paso del flujo: store=Store(this)
  store=Store(this)
  // Paso del flujo: sensors=getSystemService(SENSOR_SERVICE) as SensorManager
  sensors=getSystemService(SENSOR_SERVICE) as SensorManager
  // Paso del flujo: query=savedInstanceState?.getString("query") ?: ""
  query=savedInstanceState?.getString("query") ?: ""
  // Paso del flujo: club=savedInstanceState?.getString("club") ?: ""
  club=savedInstanceState?.getString("club") ?: ""
  // Paso del flujo: onlyFavorites=savedInstanceState?.getBoolean("favorites") ?: false
  onlyFavorites=savedInstanceState?.getBoolean("favorites") ?: false
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val id=savedInstanceState?.getInt("selected",-1)
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val p=store.players().find { it.id==id }
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(p==null) catalog() else detail(p)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define screen; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun screen(title: String) {
  // Paso del flujo: refresh=null
  refresh=null
  // Paso del flujo: picture?.animate()?.cancel()
  picture?.animate()?.cancel()
  // Paso del flujo: picture=null
  picture=null
  // Paso del flujo: box=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(28,24,28,32); setBackgroundColor(Color.rgb(241,246,249)) }
  box=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(28,24,28,32); setBackgroundColor(Color.rgb(241,246,249)) }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val scroll=ScrollView(this).apply { addView(box) }
  // Paso del flujo: setContentView(scroll)
  setContentView(scroll)
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  scroll.setOnApplyWindowInsetsListener { v,insets ->
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if(Build.VERSION.SDK_INT>=30) { val b=insets.getInsets(WindowInsets.Type.systemBars()); v.setPadding(b.left,b.top,b.right,b.bottom) }
   // Paso del flujo: insets
   insets
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Paso del flujo: scroll.requestApplyInsets()
  scroll.requestApplyInsets()
  // Paso del flujo: text(title,25f)
  text(title,25f)
  // Paso del flujo: status=text("Fútbol Aula · datos con fuente; notas personales separadas",12f)
  status=text("Fútbol Aula · datos con fuente; notas personales separadas",12f)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define text; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun text(value: String,size: Float=17f): TextView = TextView(this).apply { text=value; textSize=size; setPadding(6,10,6,10); box.addView(this) }
 // Define button; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun button(label: String,action: ()->Unit): Button = Button(this).apply { text=label; isAllCaps=false; setOnClickListener { guard(action) }; box.addView(this) }
 // Define input; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun input(hintText: String,value: String=""): EditText = EditText(this).apply { hint=hintText; setText(value); contentDescription=hintText; box.addView(this) }
 // Define guard; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 // inline permite que los return de dentro del bloque salgan de la función que llama a guard.
 private inline fun guard(action: ()->Unit) {
  // Comunica el resultado o el estado al usuario de forma visible.
  try { action() } catch(e: NotImplementedError) { status.text="Ejercicio pendiente. Abre Academia y consulta el ejemplo asociado." } catch(e: Exception) { status.text="No se pudo completar: ${e.message}" }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define task; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun task(action: ()->String) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val target=status
  // Paso del flujo: target.text="Procesando…"
  target.text="Procesando…"
  // Paso del flujo: worker.execute {
  worker.execute {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val result=try { action() } catch(e: NotImplementedError) { "Ejercicio pendiente: consulta Academia" } catch(e: Exception) { "Error: ${e.message}" }
   // Paso del flujo: runOnUiThread { if(!isDestroyed && status===target) target.text=result }
   runOnUiThread { if(!isDestroyed && status===target) target.text=result }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define spinner; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun spinner(items: List<String>,value: String,onChange: (String)->Unit) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val s=Spinner(this)
  // Paso del flujo: s.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,items)
  s.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,items)
  // Paso del flujo: s.setSelection(items.indexOf(value).coerceAtLeast(0))
  s.setSelection(items.indexOf(value).coerceAtLeast(0))
  // Paso del flujo: box.addView(s)
  box.addView(s)
  // Paso del flujo: s.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
  s.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
   // Implementa la respuesta al evento onItemSelected.
   override fun onItemSelected(parent: AdapterView<*>?,view: android.view.View?,index: Int,id: Long) { onChange(items[index]) }
   // Implementa la respuesta al evento onNothingSelected.
   override fun onNothingSelected(parent: AdapterView<*>?) {}
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define catalog; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun catalog() {
  // Paso del flujo: selected=null
  selected=null
  // Paso del flujo: releaseAudio()
  releaseAudio()
  // Paso del flujo: screen("Fútbol Fichas")
  screen("Fútbol Fichas")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val menuButton=button("Menú de la aplicación") {}
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  menuButton.setOnClickListener {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val menu=PopupMenu(this,menuButton)
   // Paso del flujo: listOf("Academia: 16 parejas","Añadir equipos","Comparar jugadores","Alineación 4-3-3","Mis colecciones","Copia de seguridad","Datos sin conexión","Cuestionario","Panel personal","Recibir por Bluetooth","Acerca de").forEach { menu.menu.add(it) }
   listOf("Academia: 16 parejas","Añadir equipos","Comparar jugadores","Alineación 4-3-3","Mis colecciones","Copia de seguridad","Datos sin conexión","Cuestionario","Panel personal","Recibir por Bluetooth","Acerca de").forEach { menu.menu.add(it) }
   // Asocia un manejador al evento de interfaz o multimedia indicado.
   menu.setOnMenuItemClickListener { item -> guard {
    // Paso del flujo: when(item.title.toString()) {
    when(item.title.toString()) {
     // Paso del flujo: "Academia: 16 parejas" -> academy()
     "Academia: 16 parejas" -> academy()
     // Paso del flujo: "Añadir equipos" -> teams()
     "Añadir equipos" -> teams()
     // Paso del flujo: "Comparar jugadores" -> compare()
     "Comparar jugadores" -> compare()
     // Paso del flujo: "Alineación 4-3-3" -> lineup()
     "Alineación 4-3-3" -> lineup()
     // Paso del flujo: "Mis colecciones" -> collections()
     "Mis colecciones" -> collections()
     // Paso del flujo: "Copia de seguridad" -> backup()
     "Copia de seguridad" -> backup()
     // Paso del flujo: "Datos sin conexión" -> cacheScreen()
     "Datos sin conexión" -> cacheScreen()
     // Paso del flujo: "Cuestionario" -> quizStart()
     "Cuestionario" -> quizStart()
     // Paso del flujo: "Panel personal" -> dashboard()
     "Panel personal" -> dashboard()
     // Paso del flujo: "Recibir por Bluetooth" -> receive()
     "Recibir por Bluetooth" -> receive()
     // Paso del flujo: else -> AlertDialog.Builder(this).setTitle("Catálogo educativo").setMessage("No es una plantilla oficial ni un servicio en directo. Fuentes y fechas en FUENTES.md. Avatares generados; sin estadísticas inventadas. Notas y valoraciones son del usuario.").setPositiveButton("Cerrar",null).show()
     else -> AlertDialog.Builder(this).setTitle("Catálogo educativo").setMessage("No es una plantilla oficial ni un servicio en directo. Fuentes y fechas en FUENTES.md. Avatares generados; sin estadísticas inventadas. Notas y valoraciones son del usuario.").setPositiveButton("Cerrar",null).show()
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
   // Paso del flujo: }; true }
   }; true }
   // Paso del flujo: menu.show()
   menu.show()
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val search=input("Buscar nombre o posición",query)
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val all=store.players()
  // Paso del flujo: spinner(listOf("Todos")+all.map { it.club }.distinct(),club.ifEmpty { "Todos" }) { club=if(it=="Todos") "" else it; refresh?.invoke() }
  spinner(listOf("Todos")+all.map { it.club }.distinct(),club.ifEmpty { "Todos" }) { club=if(it=="Todos") "" else it; refresh?.invoke() }
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  box.addView(CheckBox(this).apply { text="Solo favoritos"; isChecked=onlyFavorites; setOnCheckedChangeListener { _,checked -> onlyFavorites=checked; refresh?.invoke() } })
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  box.addView(CheckBox(this).apply { text="E02: activar filtros avanzados y orden"; isChecked=advanced; setOnCheckedChangeListener { _,checked -> advanced=checked; refresh?.invoke() } })
  // Paso del flujo: spinner(listOf("Todas las posiciones","Portero","Defensa","Centrocampista","Delantero"),position.ifEmpty { "Todas las posiciones" }) { position=if(it=="Todas las posiciones") "" else it; refresh?.invoke() }
  spinner(listOf("Todas las posiciones","Portero","Defensa","Centrocampista","Delantero"),position.ifEmpty { "Todas las posiciones" }) { position=if(it=="Todas las posiciones") "" else it; refresh?.invoke() }
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  box.addView(CheckBox(this).apply { text="E02: orden descendente"; isChecked=descending; setOnCheckedChangeListener { _,checked -> descending=checked; refresh?.invoke() } })
  // Asocia un manejador al evento de interfaz o multimedia indicado.
  box.addView(Switch(this).apply { text="Agitar para una ficha sorpresa"; isChecked=shake; setOnCheckedChangeListener { _,checked -> shake=checked } })
  // Paso del flujo: text("Sin sensor también puedes usar el botón sorpresa. E05 respeta la lista visible.",12f)
  text("Sin sensor también puedes usar el botón sorpresa. E05 respeta la lista visible.",12f)
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Sorpresa básica") { val p=visible.randomOrNull(); if(p==null) status.text="Sin candidatos" else detail(p) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E05: sorpresa entre resultados") { E05Sensor.choose(visible,kotlin.random.Random.nextInt())?.let { detail(it) } ?: run { status.text="Sin candidatos" } }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val list=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL }
  // Paso del flujo: box.addView(list)
  box.addView(list)
  // Paso del flujo: refresh={ guard {
  refresh={ guard {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val rows=store.players()
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val favorites=rows.filter { store.mark(it.id).favorite }.map { it.id }.toSet()
   // Paso del flujo: visible=if(advanced) E02Filtros.filter(rows,query,club,position,favorites,onlyFavorites,descending) else rows.filter { (TextKey.of(it.name).contains(TextKey.of(query)) || TextKey.of(it.position).contains(TextKey.of(query))) && (club.isEmpty() || it.club==club) && (!onlyFavorites || it.id in favorites) }
   visible=if(advanced) E02Filtros.filter(rows,query,club,position,favorites,onlyFavorites,descending) else rows.filter { (TextKey.of(it.name).contains(TextKey.of(query)) || TextKey.of(it.position).contains(TextKey.of(query))) && (club.isEmpty() || it.club==club) && (!onlyFavorites || it.id in favorites) }
   // Paso del flujo: list.removeAllViews()
   list.removeAllViews()
   // Comunica el resultado o el estado al usuario de forma visible.
   status.text="${visible.size} jugadores · selección educativa, no plantilla completa"
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if(visible.isEmpty()) list.addView(TextView(this).apply { text="No hay coincidencias. Prueba a quitar filtros." })
   // Asocia un manejador al evento de interfaz o multimedia indicado.
   visible.forEach { p -> list.addView(Button(this).apply { text="${if(p.id in favorites) "★ " else ""}${p.name}\n${p.club} · ${p.position}"; isAllCaps=false; setOnClickListener { guard { detail(p) } } }) }
  // Paso del flujo: } }
  } }
  // Paso del flujo: search.addTextChangedListener(object: TextWatcher {
  search.addTextChangedListener(object: TextWatcher {
   // Implementa la respuesta al evento beforeTextChanged.
   override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {}
   // Implementa la respuesta al evento onTextChanged.
   override fun onTextChanged(s: CharSequence?,start: Int,before: Int,count: Int) { query=s.toString(); refresh?.invoke() }
   // Implementa la respuesta al evento afterTextChanged.
   override fun afterTextChanged(s: Editable?) {}
  // Cierra el bloque anterior y devuelve el control a su contexto.
  })
  // Paso del flujo: refresh?.invoke()
  refresh?.invoke()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define detail; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun detail(p: Player) {
  // Paso del flujo: selected=p
  selected=p
  // Paso del flujo: screen(p.name)
  screen(p.name)
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver al catálogo") { catalog() }
  // Paso del flujo: text("${p.club} · ${p.position}")
  text("${p.club} · ${p.position}")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val path=runCatching { E07Imagen.filename(p.id,"png") }.getOrNull()
  // Paso del flujo: image=path?.let { BitmapFactory.decodeFile(File(filesDir,it).path) } ?: MediaArt.card(p)
  image=path?.let { BitmapFactory.decodeFile(File(filesDir,it).path) } ?: MediaArt.card(p)
  // Paso del flujo: original=image
  original=image
  // Paso del flujo: picture=ImageView(this).apply { adjustViewBounds=true; contentDescription="Imagen de ${p.name}: avatar o aportación del usuario"; setImageBitmap(image); box.addView(this,LinearLayout.LayoutParams(-1,430)) }
  picture=ImageView(this).apply { adjustViewBounds=true; contentDescription="Imagen de ${p.name}: avatar o aportación del usuario"; setImageBitmap(image); box.addView(this,LinearLayout.LayoutParams(-1,430)) }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val m=store.mark(p.id)
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val favorite=CheckBox(this).apply { text="Favorito"; isChecked=m.favorite; box.addView(this) }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val note=input("Mis notas (no son datos oficiales)",m.note)
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Guardar favorito y nota") { require(note.text.length<=4000) { "Máximo 4000 caracteres" }; store.save(p.id,store.mark(p.id).copy(favorite=favorite.isChecked,note=note.text.toString())); status.text="Guardado en SQLite" }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Borrar mis anotaciones") { AlertDialog.Builder(this).setTitle("¿Borrar anotaciones?").setMessage("No elimina la ficha ni las imágenes.").setPositiveButton("Borrar") { _,_ -> store.delete(p.id); detail(p) }.setNegativeButton("Cancelar",null).show() }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val tags=input("E03: etiquetas separadas por coma",m.tags.joinToString(","))
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val rating=input("E03: valoración personal de 0 a 5",m.rating.toString())
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E03: guardar etiquetas y valoración") { store.save(p.id,E03Etiquetas.mark(store.mark(p.id),tags.text.toString().split(","),rating.text.toString().toInt())); status.text="Valoración personal guardada; no es estadística deportiva" }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Abrir fuente oficial") { startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(p.source))) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Consulta HTTPS de la fuente") { task { val text=download(p.source); "HTML recibido (${text.length} caracteres). No sincroniza el catálogo.\n${text.take(180)}" } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Compartir ficha como texto") { startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type="text/plain"; putExtra(Intent.EXTRA_TEXT,"${p.name} · ${p.club} · ${p.position}\n${p.source}") },"Compartir")) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Enviar texto por Bluetooth SPP") { send(false,listOf(p.id)) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Importar imagen propia") { startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { type="image/*"; addCategory(Intent.CATEGORY_OPENABLE) },2) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Capturar miniatura con cámara externa") { startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),1) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Filtro gris resuelto") { image?.let { updateImage(MediaArt.gray(it)) } }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val contrast=input("E06: contraste de 0 a 3","1.4")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E06: contraste sin perder original") { val params=E06Contraste.parameters(contrast.text.toString().toFloat()); original?.let { updateImage(MediaArt.contrast(it,params.first,params.second)) } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Recuperar original") { original?.let { updateImage(it) } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Exportar PNG y JPEG de laboratorio") { val b=image ?: return@button; task { MediaArt.save(b,File(filesDir,"export_${p.id}.png"),Bitmap.CompressFormat.PNG); MediaArt.save(b,File(filesDir,"export_${p.id}.jpg"),Bitmap.CompressFormat.JPEG); "Exportados: usa Device Explorer (app debug)" } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E07: conservar imagen para esta ficha") { val b=image ?: return@button; val name=E07Imagen.filename(p.id,"png"); task { MediaArt.save(b,File(filesDir,name),Bitmap.CompressFormat.PNG); "Imagen persistida" } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Animar ficha") { picture?.animate()?.rotationYBy(360f)?.setDuration(800)?.start() }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val title=input("E14: título de cromo (1–28 caracteres)","Mi colección")
  // Reserva un estado que podrá actualizarse al producirse eventos.
  var color=0
  // Paso del flujo: spinner(listOf("Azul","Violeta","Verde"),"Azul") { color=listOf("Azul","Violeta","Verde").indexOf(it) }
  spinner(listOf("Azul","Violeta","Verde"),"Azul") { color=listOf("Azul","Violeta","Verde").indexOf(it) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E14: previsualizar cromo") { val clean=E14Cromo.title(title.text.toString(),color); updateImage(MediaArt.card(p,clean,color)) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Compartir imagen actual") {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val b=image ?: return@button
   // Paso del flujo: MediaArt.save(b,File(cacheDir,"cromo.png"),Bitmap.CompressFormat.PNG)
   MediaArt.save(b,File(cacheDir,"cromo.png"),Bitmap.CompressFormat.PNG)
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val uri=Uri.parse("content://es.aula.futbol.cards/cromo.png")
   // Paso del flujo: startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type="image/png"; putExtra(Intent.EXTRA_STREAM,uri); clipData=ClipData.newRawUri("Cromo",uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) },"Compartir imagen"))
   startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type="image/png"; putExtra(Intent.EXTRA_STREAM,uri); clipData=ClipData.newRawUri("Cromo",uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) },"Compartir imagen"))
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Seleccionar audio o audio de vídeo") { startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { type="*/*"; putExtra(Intent.EXTRA_MIME_TYPES,arrayOf("audio/*","video/*")); addCategory(Intent.CATEGORY_OPENABLE) },3) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Pausar / continuar") { if(prepared && recorder==null) audio?.let { if(it.isPlaying) it.pause() else it.start() } }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Detener reproducción") { releaseAudio() }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Grabar comentario de voz propio") { record() }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Detener y guardar grabación") { stopRecording(true) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Escuchar mis comentarios de esta ficha") { recordings(p) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E08: audio anterior") { moveAudio(-1) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E08: audio siguiente") { moveAudio(1) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Eliminar comentario seleccionado") { audioFiles.getOrNull(audioIndex)?.let { file -> AlertDialog.Builder(this).setTitle("¿Eliminar ${file.name}?").setPositiveButton("Eliminar") { _,_ -> releaseAudio(); file.delete(); status.text="Audio eliminado" }.setNegativeButton("Cancelar",null).show() } }
  // Paso del flujo: text("Usa solo imágenes y audios propios o autorizados. Importar no confirma la identidad del futbolista. El vídeo solo reproduce su pista de audio.",12f)
  text("Usa solo imágenes y audios propios o autorizados. Importar no confirma la identidad del futbolista. El vídeo solo reproduce su pista de audio.",12f)
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define updateImage; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun updateImage(b: Bitmap) { image=b; picture?.setImageBitmap(b) }
 // Define academy; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun academy() {
  // Paso del flujo: selected=null
  selected=null
  // Paso del flujo: screen("Academia: ejemplos y evaluables")
  screen("Academia: ejemplos y evaluables")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Crea el botón descrito y asocia la acción a su pulsación: comprueba los 16 a la vez.
  button("Comprobar los 16 ejercicios") { checkSummary() }
  // Comunica el resultado o el estado al usuario de forma visible.
  text("1. Lee el enunciado. 2. Ejecuta el ejemplo. 3. Abre el .kt evaluable. 4. Haz un paso. 5. Pulsa Comprobar E## para verificarlo. Los pendientes no cierran la app.")
  // Paso del flujo: Academy.titles.forEachIndexed { index,title ->
  Academy.titles.forEachIndexed { index,title ->
   // Paso del flujo: text("${index+1}. $title",20f)
   text("${index+1}. $title",20f)
   // Muestra el último resultado conocido de este ejercicio, si ya se comprobó.
   checkStates[index+1]?.let { state -> checkBadge(state) }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Leer enunciado E%02d".format(index+1)) { val content=assets.open("E%02d.md".format(index+1)).bufferedReader().use { it.readText() }; screen(title); button("Volver a Academia") { academy() }; text(content,16f) }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Ejemplo resuelto X%02d".format(index+1)) { status.text=Academy.example(index) }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Probar evaluable E%02d".format(index+1)) { status.text=Academy.exercise(index) }
   // Crea el botón descrito y asocia la acción a su pulsación: ejecuta el comprobador del ejercicio.
   button("Comprobar E%02d".format(index+1)) { checkDetail(index+1) }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define checkBadge; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 // Muestra el estado de un ejercicio con un icono y un color: verde, rojo o gris.
 private fun checkBadge(state: CheckState) {
  // Crea una línea de texto con el mensaje según el estado del ejercicio.
  val view=text(when(state) {
   // Verde: todos los casos del ejercicio han pasado.
   CheckState.CORRECTO -> "✓ CORRECTO: supera todos los casos"
   // Rojo: algún caso falla; hay que revisar el detalle.
   CheckState.INCORRECTO -> "✗ INCORRECTO: abre el detalle para ver los fallos"
   // Gris: el ejercicio sigue sin implementar (TODO).
   CheckState.PENDIENTE -> "⏳ PENDIENTE: aún sin implementar"
  },14f)
  // Pinta la línea con el color del estado para reconocerlo de un vistazo.
  view.setTextColor(when(state) {
   // Verde correcto, rojo incorrecto y gris pendiente.
   CheckState.CORRECTO -> Color.rgb(16,124,62); CheckState.INCORRECTO -> Color.rgb(198,40,40); CheckState.PENDIENTE -> Color.rgb(110,110,110)
  })
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define checkSummary; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 // Ejecuta el comprobador de los 16 ejercicios y muestra un resumen rápido con colores.
 private fun checkSummary() {
  // Paso del flujo: screen("Comprobar los 16 ejercicios")
  screen("Comprobar los 16 ejercicios")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver a Academia") { academy() }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver al catálogo") { catalog() }
  // Paso del flujo: val results=Checker.all()
  // Ejecuta las 16 comprobaciones: son funciones Kotlin puras y tardan un instante.
  val results=Checker.all()
  // Guarda cada estado para que Academia lo muestre al volver.
  results.forEach { checkStates[it.exercise]=it.state }
  // Paso del flujo: val ok=results.count { it.state==CheckState.CORRECTO }
  val ok=results.count { it.state==CheckState.CORRECTO }
  // Paso del flujo: val bad=results.count { it.state==CheckState.INCORRECTO }
  val bad=results.count { it.state==CheckState.INCORRECTO }
  // Paso del flujo: val pending=results.count { it.state==CheckState.PENDIENTE }
  val pending=results.count { it.state==CheckState.PENDIENTE }
  // Comunica el resultado o el estado al usuario de forma visible.
  text("Resumen: $ok correctos · $bad incorrectos · $pending pendientes. Pulsa un ejercicio para ver su detalle.",18f)
  // Paso del flujo: results.forEach { r ->
  results.forEach { r ->
   // Crea el botón descrito y asocia la acción a su pulsación: abre el detalle del ejercicio.
   val b=button("E%02d · ${r.title}".format(r.exercise)) { checkDetail(r.exercise) }
   // Pinta el botón con el color del estado para reconocerlo de un vistazo.
   b.setTextColor(when(r.state) {
    // Verde correcto, rojo incorrecto y gris pendiente.
    CheckState.CORRECTO -> Color.rgb(16,124,62); CheckState.INCORRECTO -> Color.rgb(198,40,40); CheckState.PENDIENTE -> Color.rgb(110,110,110)
   })
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define checkDetail; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 // Comprueba UN ejercicio y muestra todos sus casos con resultado esperado y obtenido.
 private fun checkDetail(n: Int) {
  // Paso del flujo: val r=Checker.check(n)
  val r=Checker.check(n)
  // Guarda el estado para que Academia lo muestre al volver.
  checkStates[n]=r.state
  // Paso del flujo: screen("Comprobación E%02d · ${r.title}".format(n))
  screen("Comprobación E%02d · ${r.title}".format(n))
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver a Academia") { academy() }
  // Crea el botón descrito y asocia la acción a su pulsación: vuelve a ejecutar la comprobación.
  button("Comprobar otra vez") { checkDetail(n) }
  // Muestra el estado global con su color.
  checkBadge(r.state)
  // Paso del flujo: r.cases.forEach { c ->
  r.cases.forEach { c ->
   // Crea una línea por caso con su icono: verde, rojo o gris.
   val line=text((if(c.pending) "⏳" else if(c.ok) "✓" else "✗")+" "+c.name,15f)
   // Pinta la línea del caso con el color que le toca.
   line.setTextColor(if(c.pending) Color.rgb(110,110,110) else if(c.ok) Color.rgb(16,124,62) else Color.rgb(198,40,40))
   // Muestra la explicación del fallo o del pendiente debajo del caso.
   if(!c.ok) text(c.detail,12f)
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Muestra lo que no se puede comprobar automáticamente y requiere un móvil.
  r.deviceNote?.let { note -> text("Prueba además en tu móvil: $note",13f) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define teams; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun teams() {
  // Paso del flujo: screen("E01 · Añadir equipos reales")
  screen("E01 · Añadir equipos reales")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Paso del flujo: text("Una línea por jugador: id;nombre;club;posición;https://fuente. Un solo club por lote. Mantén los IDs existentes. Son datos con fecha de referencia, no plantillas en directo.")
  text("Una línea por jugador: id;nombre;club;posición;https://fuente. Un solo club por lote. Mantén los IDs existentes. Son datos con fecha de referencia, no plantillas en directo.")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val data=input("Pega las líneas verificadas")
  // Paso del flujo: data.minLines=5
  data.minLines=5
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Validar y añadir lote") {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val incoming=data.text.toString().lines().filter { it.isNotBlank() }.map { line -> val c=line.split(';'); require(c.size==5) { "Cinco campos por línea" }; Player(c[0].trim().toInt(),c[1].trim(),c[2].trim(),c[3].trim(),c[4].trim()) }
   // Comunica el resultado o el estado al usuario de forma visible.
   store.players(E01Catalogo.addTeam(store.players(),incoming)); status.text="Lote guardado"
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Cargar dos lotes de referencia resueltos (E01)") {
   // Reserva un estado que podrá actualizarse al producirse eventos.
   var rows=store.players()
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if(rows.none { it.id==101 }) rows=E01Catalogo.addTeam(rows,Seed.extra)
   // Evalúa la condición y ejecuta únicamente el camino correspondiente.
   if(rows.none { it.id==201 }) rows=E01Catalogo.addTeam(rows,Seed.atleti)
   // Comunica el resultado o el estado al usuario de forma visible.
   store.players(rows); status.text="Cuatro clubes disponibles. Revisa FUENTES.md: son selecciones, no plantillas completas."
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define pickMany; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun pickMany(title: String,chosen: Set<Int> = emptySet(),done: (List<Player>)->Unit) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val rows=store.players()
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val selectedIds=chosen.toMutableSet()
  // Paso del flujo: AlertDialog.Builder(this).setTitle(title).setMultiChoiceItems(rows.map { it.name }.toTypedArray(),rows.map { it.id in chosen }.toBooleanArray()) { _,i,checked -> if(checked) selectedIds.add(rows[i].id) else selectedIds.remove(rows[i].id) }.setPositiveButton("Aceptar") { _,_ -> guard { done(rows.filter { it.id in selectedIds }) } }.setNegativeButton("Cancelar",null).show()
  AlertDialog.Builder(this).setTitle(title).setMultiChoiceItems(rows.map { it.name }.toTypedArray(),rows.map { it.id in chosen }.toBooleanArray()) { _,i,checked -> if(checked) selectedIds.add(rows[i].id) else selectedIds.remove(rows[i].id) }.setPositiveButton("Aceptar") { _,_ -> guard { done(rows.filter { it.id in selectedIds }) } }.setNegativeButton("Cancelar",null).show()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define compare; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun compare() {
  // Paso del flujo: screen("E04 · Comparador")
  screen("E04 · Comparador")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Paso del flujo: text("Compara identidad y posición. No hay puntuaciones oficiales ni un ganador inventado.")
  text("Compara identidad y posición. No hay puntuaciones oficiales ni un ganador inventado.")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Elegir de dos a cuatro jugadores") { pickMany("Comparar") { status.text=E04Comparador.compare(it) } }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define lineup; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun lineup() {
  // Paso del flujo: screen("E09 · Mi once 4-3-3")
  screen("E09 · Mi once 4-3-3")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Paso del flujo: text("Pulsa un espacio para sustituirlo. Solo se muestran jugadores compatibles y no repetidos. Puedes guardar una alineación incompleta.")
  text("Pulsa un espacio para sustituirlo. Solo se muestran jugadores compatibles y no repetidos. Puedes guardar una alineación incompleta.")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val slots=listOf("Portero")+List(4){"Defensa"}+List(3){"Centrocampista"}+List(3){"Delantero"}
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val saved=JSONArray(store.get("lineup","[]"))
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val ids=MutableList(11) { if(it<saved.length()) saved.optInt(it,0) else 0 }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val rows=store.players()
  // Paso del flujo: slots.forEachIndexed { i,pos ->
  slots.forEachIndexed { i,pos ->
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("${i+1}. $pos: ${rows.find { it.id==ids[i] }?.name ?: "vacío"}") {
    // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
    val candidates=rows.filter { it.position==pos && (it.id !in ids || it.id==ids[i]) }
    // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
    val labels=listOf("Quitar jugador")+candidates.map { it.name }
    // Paso del flujo: AlertDialog.Builder(this).setTitle(pos).setItems(labels.toTypedArray()) { _,index -> ids[i]=if(index==0) 0 else candidates[index-1].id; store.put("lineup",JSONArray(ids).toString()); lineup() }.show()
    AlertDialog.Builder(this).setTitle(pos).setItems(labels.toTypedArray()) { _,index -> ids[i]=if(index==0) 0 else candidates[index-1].id; store.put("lineup",JSONArray(ids).toString()); lineup() }.show()
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E09: comprobar once completo") { val selected=ids.mapNotNull { id -> rows.find { it.id==id } }; status.text=if(E09Alineacion.validate(selected)) "Once válido" else "Incompleto o inválido: ${selected.size}/11. Necesitas más jugadores de las posiciones que faltan." }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define collections; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun collections() {
  // Paso del flujo: screen("E10 · Mis colecciones")
  screen("E10 · Mis colecciones")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val root=JSONObject(store.get("collections","{}"))
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val name=input("Nombre de colección")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Crear colección") { val names=root.keys().asSequence().toList(); val new=E10Colecciones.add(names,name.text.toString()).last(); root.put(new,JSONArray()); store.put("collections",root.toString()); collections() }
  // Paso del flujo: root.keys().asSequence().toList().sorted().forEach { key ->
  root.keys().asSequence().toList().sorted().forEach { key ->
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val array=root.getJSONArray(key)
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val ids=(0 until array.length()).map { array.getInt(it) }.toSet()
   // Paso del flujo: text("$key (${ids.size})",20f)
   text("$key (${ids.size})",20f)
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Editar integrantes: $key") { pickMany(key,ids) { rows -> root.put(key,JSONArray(rows.map { it.id })); store.put("collections",root.toString()); collections() } }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Renombrar: $key") { val field=EditText(this).apply { setText(key) }; AlertDialog.Builder(this).setTitle("Nuevo nombre").setView(field).setPositiveButton("Guardar") { _,_ -> guard { val rest=root.keys().asSequence().filter { it!=key }.toList(); val new=E10Colecciones.add(rest,field.text.toString()).last(); root.remove(key); root.put(new,array); store.put("collections",root.toString()); collections() } }.setNegativeButton("Cancelar",null).show() }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("E15: enviar $key por Bluetooth con ACK") { send(true,ids.toList()) }
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Eliminar colección $key") { AlertDialog.Builder(this).setTitle("¿Eliminar solo la colección?").setPositiveButton("Eliminar") { _,_ -> root.remove(key); store.put("collections",root.toString()); collections() }.setNegativeButton("Cancelar",null).show() }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define backup; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun backup() {
  // Paso del flujo: screen("E11 · Copia de seguridad")
  screen("E11 · Copia de seguridad")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Paso del flujo: text("Formato versión 1: favoritos y notas. No incluye audios, etiquetas, imágenes, catálogo ni colecciones. La restauración conserva esos datos. Primero valida, después pide confirmación y escribe en transacción.")
  text("Formato versión 1: favoritos y notas. No incluye audios, etiquetas, imágenes, catálogo ni colecciones. La restauración conserva esos datos. Primero valida, después pide confirmación y escribe en transacción.")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Exportar JSON") { startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply { type="application/json"; putExtra(Intent.EXTRA_TITLE,"futbol-copia.json"); addCategory(Intent.CATEGORY_OPENABLE) },11) }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("E11: importar y previsualizar copia") { startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { type="application/json"; addCategory(Intent.CATEGORY_OPENABLE) },12) }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define download; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun download(url: String): String {
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(url.startsWith("https://")) { "Solo HTTPS" }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val c=URL(url).openConnection() as HttpsURLConnection
  // Paso del flujo: try {
  try {
   // Paso del flujo: c.connectTimeout=8000; c.readTimeout=8000
   c.connectTimeout=8000; c.readTimeout=8000
   // Paso del flujo: c.setRequestProperty("User-Agent","FutbolAula-Educativo/2")
   c.setRequestProperty("User-Agent","FutbolAula-Educativo/2")
   // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
   check(c.responseCode in 200..299) { "HTTP ${c.responseCode}" }
   // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
   return c.inputStream.bufferedReader().use { it.readText().also { text -> require(text.length<=1000000) { "Respuesta demasiado grande" } } }
  // Paso del flujo: } finally { c.disconnect() }
  } finally { c.disconnect() }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define cacheScreen; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun cacheScreen() {
  // Paso del flujo: screen("E12 · Catálogo sin conexión")
  screen("E12 · Catálogo sin conexión")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Paso del flujo: text("URL HTTPS que devuelva un array JSON de objetos id,name,club,position,source. No es la URL HTML de un club. Debes usar un endpoint autorizado; no hay claves incrustadas. Se incluye plantilla JSON en datos/.")
  text("URL HTTPS que devuelva un array JSON de objetos id,name,club,position,source. No es la URL HTML de un club. Debes usar un endpoint autorizado; no hay claves incrustadas. Se incluye plantilla JSON en datos/.")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val url=input("Endpoint HTTPS",store.get("endpoint"))
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Actualizar catálogo desde JSON") {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val endpoint=url.text.toString().trim(); store.put("endpoint",endpoint)
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val saved=store.players()
   // Paso del flujo: task {
   task {
    // Reserva un estado que podrá actualizarse al producirse eventos.
    var error: String?=null
    // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
    val remote=try {
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val a=JSONArray(download(endpoint))
     // Paso del flujo: (0 until a.length()).map { i -> val p=a.getJSONObject(i); Player(p.getInt("id"),p.getString("name"),p.getString("club"),p.getString("position"),p.getString("source")) }.also { rows -> require(rows.isNotEmpty()); require(rows.map { it.id }.distinct().size==rows.size); require(rows.all { it.name.isNotBlank() && it.club.isNotBlank() && it.source.startsWith("https://") && it.position in setOf("Portero","Defensa","Centrocampista","Delantero") }) }
     (0 until a.length()).map { i -> val p=a.getJSONObject(i); Player(p.getInt("id"),p.getString("name"),p.getString("club"),p.getString("position"),p.getString("source")) }.also { rows -> require(rows.isNotEmpty()); require(rows.map { it.id }.distinct().size==rows.size); require(rows.all { it.name.isNotBlank() && it.club.isNotBlank() && it.source.startsWith("https://") && it.position in setOf("Portero","Defensa","Centrocampista","Delantero") }) }
    // Paso del flujo: } catch(e: Exception) { error=e.message; null }
    } catch(e: Exception) { error=e.message; null }
    // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
    val result=E12Cache.resolve(remote,saved,error)
    // Evalúa la condición y ejecuta únicamente el camino correspondiente.
    if(!result.stale) { store.players(result.rows); store.put("updated",java.text.DateFormat.getDateTimeInstance().format(java.util.Date())) }
    // Paso del flujo: "${result.message}. Última actualización: ${store.get("updated","catálogo inicial")}. ${result.rows.size} jugadores."
    "${result.message}. Última actualización: ${store.get("updated","catálogo inicial")}. ${result.rows.size} jugadores."
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define quizStart; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun quizStart() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val questions=E13Quiz.build(store.players().shuffled(),5)
  // Reserva un estado que podrá actualizarse al producirse eventos.
  var index=0
  // Reserva un estado que podrá actualizarse al producirse eventos.
  var score=0
  // Define showQuestion; sus parámetros indican los datos de entrada y el tipo final indica la salida.
  fun showQuestion() {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val q=questions[index]
   // Reserva un estado que podrá actualizarse al producirse eventos.
   var answered=false
   // Paso del flujo: screen("E13 · Pregunta ${index+1}/${questions.size}")
   screen("E13 · Pregunta ${index+1}/${questions.size}")
   // Crea el botón descrito y asocia la acción a su pulsación.
   button("Abandonar") { catalog() }
   // Paso del flujo: text(q.text,22f)
   text(q.text,22f)
   // Paso del flujo: q.options.forEach { option -> button(option) {
   q.options.forEach { option -> button(option) {
    // Evalúa la condición y ejecuta únicamente el camino correspondiente.
    if(!answered) {
     // Paso del flujo: answered=true
     answered=true
     // Evalúa la condición y ejecuta únicamente el camino correspondiente.
     if(option==q.answer) score++
     // Comunica el resultado o el estado al usuario de forma visible.
     status.text="Correcta: ${q.answer}. Puntos: $score"
     // Crea el botón descrito y asocia la acción a su pulsación.
     button("Continuar") {
      // Evalúa la condición y ejecuta únicamente el camino correspondiente.
      if(index<questions.lastIndex) { index++; showQuestion() } else {
       // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
       val best=maxOf(score,store.get("best","0").toInt())
       // Paso del flujo: store.put("best",best.toString())
       store.put("best",best.toString())
       // Paso del flujo: screen("Resultado: $score/5")
       screen("Resultado: $score/5")
       // Paso del flujo: text("Mejor resultado personal: $best/5")
       text("Mejor resultado personal: $best/5")
       // Crea el botón descrito y asocia la acción a su pulsación.
       button("Volver") { catalog() }
      // Cierra el bloque anterior y devuelve el control a su contexto.
      }
     // Cierra el bloque anterior y devuelve el control a su contexto.
     }
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
   // Paso del flujo: } }
   } }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Paso del flujo: showQuestion()
  showQuestion()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define dashboard; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun dashboard() {
  // Paso del flujo: screen("E16 · Panel personal")
  screen("E16 · Panel personal")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { catalog() }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val rows=store.players().filter { store.mark(it.id).favorite }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val counts=E16Panel.counts(rows)
  // Paso del flujo: text("Favoritos: ${rows.size}. Son recuentos de tu colección, no estadísticas de los futbolistas.")
  text("Favoritos: ${rows.size}. Son recuentos de tu colección, no estadísticas de los futbolistas.")
  // Paso del flujo: counts.forEach { (club,count) -> text("$club: $count"); box.addView(ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal).apply { max=maxOf(1,rows.size); progress=count; contentDescription="$club, $count de ${rows.size}" }) }
  counts.forEach { (club,count) -> text("$club: $count"); box.addView(ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal).apply { max=maxOf(1,rows.size); progress=count; contentDescription="$club, $count de ${rows.size}" }) }
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(rows.isEmpty()) text("Añade favoritos para ver el gráfico.")
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define adapter; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun adapter(): android.bluetooth.BluetoothAdapter {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val a=getSystemService(BluetoothManager::class.java)?.adapter ?: error("Sin Bluetooth")
  // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
  @SuppressLint("MissingPermission")
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val enabled=a.isEnabled
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  check(enabled) { "Activa Bluetooth desde ajustes" }
  // Devuelve este resultado al código que invocó la función.
  return a
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define permissionBluetooth; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun permissionBluetooth(): Boolean {
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(Build.VERSION.SDK_INT>=31 && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)!=PackageManager.PERMISSION_GRANTED) { requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT),40); return false }
  // Devuelve este resultado al código que invocó la función.
  return true
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @SuppressLint("MissingPermission")
 // Define send; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun send(ack: Boolean,ids: List<Int>) {
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(!permissionBluetooth()) return
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(ids.isNotEmpty()) { "Colección vacía" }
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val payload=if(ack) JSONObject().put("version",1).put("ids",JSONArray(ids)).toString() else store.players().find { it.id==ids.first() }?.let { "${it.id};${it.name};${it.club};${it.position}" } ?: error("Jugador ausente")
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(ack) E15Bluetooth.decode(payload,store.players().map { it.id }.toSet())
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val devices=adapter().bondedDevices.toList()
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(devices.isNotEmpty()) { "Empareja un servidor SPP desde ajustes" }
  // Comunica el resultado o el estado al usuario de forma visible.
  AlertDialog.Builder(this).setTitle("Receptor SPP compatible").setItems(devices.map { "${it.name} ${it.address}" }.toTypedArray()) { _,i -> task { link.send(devices[i],payload,ack) }; button("Cancelar conexión") { link.cancel(); status.text="Cancelación solicitada" } }.setNegativeButton("Cancelar",null).show()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define receive; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun receive() {
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(!permissionBluetooth()) return
  // Paso del flujo: screen("E15 · Recibir colección")
  screen("E15 · Recibir colección")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Volver") { link.cancel(); catalog() }
  // Paso del flujo: text("Empareja desde ajustes ambos equipos y usa el envío con ACK en el otro dispositivo. Espera máxima: 30 segundos. El ACK confirma formato válido, no aceptación por el usuario.")
  text("Empareja desde ajustes ambos equipos y usa el envío con ACK en el otro dispositivo. Espera máxima: 30 segundos. El ACK confirma formato válido, no aceptación por el usuario.")
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Escuchar una colección") {
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val a=adapter()
   // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
   val known=store.players().map { it.id }.toSet()
   // Paso del flujo: task {
   task {
    // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
    val ids=link.receive(a) { E15Bluetooth.decode(it,known) }
    // Comunica el resultado o el estado al usuario de forma visible.
    runOnUiThread { if(!isDestroyed) AlertDialog.Builder(this).setTitle("Colección recibida: ${ids.size} jugadores").setMessage("¿Guardar como Recibida? Si existe, se reemplaza solo esa colección.").setPositiveButton("Guardar") { _,_ -> val root=JSONObject(store.get("collections","{}")); root.put("Recibida",JSONArray(ids)); store.put("collections",root.toString()); status.text="Guardada" }.setNegativeButton("Rechazar",null).show() }
    // Paso del flujo: "Mensaje válido recibido; pendiente de aceptación"
    "Mensaje válido recibido; pendiente de aceptación"
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Crea el botón descrito y asocia la acción a su pulsación.
  button("Cancelar escucha") { link.cancel(); status.text="Cancelación solicitada" }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define play; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun play(uri: Uri) {
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(recorder==null) { "Detén primero la grabación" }
  // Paso del flujo: releaseAudio()
  releaseAudio()
  // Paso del flujo: audio=MediaPlayer().apply {
  audio=MediaPlayer().apply {
   // Paso del flujo: setDataSource(this@MainActivity,uri)
   setDataSource(this@MainActivity,uri)
   // Asocia un manejador al evento de interfaz o multimedia indicado.
   setOnPreparedListener { prepared=true; it.start(); status.text="Reproduciendo" }
   // Asocia un manejador al evento de interfaz o multimedia indicado.
   setOnCompletionListener { status.text="Fin del audio"; releaseAudio() }
   // Asocia un manejador al evento de interfaz o multimedia indicado.
   setOnErrorListener { _,what,extra -> status.text="Error de audio $what/$extra"; releaseAudio(); true }
   // Prepara el reproductor sin bloquear y espera el evento de preparación.
   prepareAsync()
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define recordings; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun recordings(p: Player) {
  // Paso del flujo: audioFiles=filesDir.listFiles()?.filter { it.name.startsWith("audio_${p.id}_") && it.extension=="m4a" }?.sortedBy { it.name } ?: emptyList()
  audioFiles=filesDir.listFiles()?.filter { it.name.startsWith("audio_${p.id}_") && it.extension=="m4a" }?.sortedBy { it.name } ?: emptyList()
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(audioFiles.isNotEmpty()) { "No hay comentarios grabados" }
  // Paso del flujo: AlertDialog.Builder(this).setTitle("Comentarios propios").setItems(audioFiles.map { it.name }.toTypedArray()) { _,i -> guard { audioIndex=i; play(Uri.fromFile(audioFiles[i])) } }.show()
  AlertDialog.Builder(this).setTitle("Comentarios propios").setItems(audioFiles.map { it.name }.toTypedArray()) { _,i -> guard { audioIndex=i; play(Uri.fromFile(audioFiles[i])) } }.show()
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define moveAudio; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun moveAudio(direction: Int) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val next=E08Audio.next(audioIndex,audioFiles.size,direction) ?: error("Selecciona antes un comentario grabado")
  // Paso del flujo: audioIndex=next
  audioIndex=next
  // Paso del flujo: play(Uri.fromFile(audioFiles[next]))
  play(Uri.fromFile(audioFiles[next]))
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @Suppress("DEPRECATION")
 // Define record; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun record() {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val p=selected ?: return
  // Verifica una condición obligatoria; si falla, evita continuar con datos inválidos.
  require(recorder==null) { "Ya estás grabando" }
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED) { requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),41); return }
  // Paso del flujo: releaseAudio()
  releaseAudio()
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val file=File(filesDir,"audio_${p.id}_${System.currentTimeMillis()}.m4a")
  // Paso del flujo: recordingFile=file
  recordingFile=file
  // Paso del flujo: try {
  try {
   // Paso del flujo: recorder=if(Build.VERSION.SDK_INT>=31) MediaRecorder(this) else MediaRecorder()
   recorder=if(Build.VERSION.SDK_INT>=31) MediaRecorder(this) else MediaRecorder()
   // Paso del flujo: recorder!!.apply { setAudioSource(MediaRecorder.AudioSource.MIC); setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); setAudioEncoder(MediaRecorder.AudioEncoder.AAC); setOutputFile(file.path); prepare(); start() }
   recorder!!.apply { setAudioSource(MediaRecorder.AudioSource.MIC); setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); setAudioEncoder(MediaRecorder.AudioEncoder.AAC); setOutputFile(file.path); prepare(); start() }
   // Comunica el resultado o el estado al usuario de forma visible.
   status.text="GRABANDO comentario propio. Pulsa Detener y guardar. Al salir se descarta."
  // Paso del flujo: } catch(e: Exception) { stopRecording(false); throw e }
  } catch(e: Exception) { stopRecording(false); throw e }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define stopRecording; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun stopRecording(save: Boolean) {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val r=recorder ?: return
  // Reserva un estado que podrá actualizarse al producirse eventos.
  var valid=save
  // Paso del flujo: try { r.stop() } catch(e: Exception) { valid=false } finally { r.release(); recorder=null }
  try { r.stop() } catch(e: Exception) { valid=false } finally { r.release(); recorder=null }
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(!valid) recordingFile?.delete()
  // Paso del flujo: recordingFile=null
  recordingFile=null
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(::status.isInitialized) status.text=if(valid) "Comentario guardado" else "Grabación descartada"
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define releaseAudio; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun releaseAudio() { prepared=false; audio?.release(); audio=null }
 // Implementa la respuesta al evento onRequestPermissionsResult.
 override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
  // Mantiene el comportamiento heredado de Android en este evento.
  super.onRequestPermissionsResult(requestCode,permissions,grantResults)
  // Comunica el resultado o el estado al usuario de forma visible.
  status.text=if(grantResults.firstOrNull()==PackageManager.PERMISSION_GRANTED) "Permiso concedido. Pulsa otra vez la acción." else "Permiso denegado. Puedes seguir usando el catálogo."
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
 @Deprecated("API clásica para una base sin librerías de interfaz")
 // Implementa la respuesta al evento onActivityResult.
 override fun onActivityResult(requestCode: Int,resultCode: Int,data: Intent?) {
  // Mantiene el comportamiento heredado de Android en este evento.
  super.onActivityResult(requestCode,resultCode,data)
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(resultCode!=RESULT_OK) return
  // Paso del flujo: guard {
  guard {
   // Paso del flujo: when(requestCode) {
   when(requestCode) {
    // Paso del flujo: 1 -> {
    1 -> {
     // Aplica metadatos de compilación o ejecución; no concede permisos al usuario.
     @Suppress("DEPRECATION")
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val b=data?.extras?.get("data") as? Bitmap
     // Evalúa la condición y ejecuta únicamente el camino correspondiente.
     if(b!=null && selected!=null) { original=b; updateImage(b) }
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
    // Paso del flujo: 2 -> {
    2 -> {
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val uri=data?.data ?: return
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val playerId=selected?.id ?: return
     // Paso del flujo: task {
     task {
      // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
      val bounds=BitmapFactory.Options().apply { inJustDecodeBounds=true }
      // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
      contentResolver.openInputStream(uri).use { BitmapFactory.decodeStream(it,null,bounds) }
      // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
      val opts=BitmapFactory.Options()
      // Paso del flujo: while(bounds.outWidth/opts.inSampleSize>1400 || bounds.outHeight/opts.inSampleSize>1400) opts.inSampleSize*=2
      while(bounds.outWidth/opts.inSampleSize>1400 || bounds.outHeight/opts.inSampleSize>1400) opts.inSampleSize*=2
      // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
      val b=contentResolver.openInputStream(uri).use { BitmapFactory.decodeStream(it,null,opts) } ?: error("Imagen no compatible")
      // Paso del flujo: runOnUiThread { if(!isDestroyed && selected?.id==playerId) { original=b; updateImage(b) } }
      runOnUiThread { if(!isDestroyed && selected?.id==playerId) { original=b; updateImage(b) } }
      // Paso del flujo: "Imagen propia importada. E07 permite conservarla."
      "Imagen propia importada. E07 permite conservarla."
     // Cierra el bloque anterior y devuelve el control a su contexto.
     }
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
    // Paso del flujo: 3 -> data?.data?.let { play(it) }
    3 -> data?.data?.let { play(it) }
    // Paso del flujo: 11 -> {
    11 -> {
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val uri=data?.data ?: return
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val payload=Wire.encode(store.marks(store.players().map { it.id }))
     // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
     task { contentResolver.openOutputStream(uri,"wt").use { requireNotNull(it).write(payload.toByteArray(Charsets.UTF_8)) }; "Copia exportada" }
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
    // Paso del flujo: 12 -> {
    12 -> {
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val uri=data?.data ?: return
     // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
     val current=store.marks(store.players().map { it.id })
     // Paso del flujo: task {
     task {
      // Usa un recurso y asegura su cierre al salir del bloque, también si se produce una excepción.
      val text=requireNotNull(contentResolver.openInputStream(uri)).bufferedReader().use { it.readText() }
      // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
      val result=E11Copia.restore(current,text,current.keys)
      // Comunica el resultado o el estado al usuario de forma visible.
      runOnUiThread { if(!isDestroyed) AlertDialog.Builder(this).setTitle("Copia válida").setMessage("Se revisarán ${result.size} registros. Solo se restauran favorito y nota; etiquetas y valoración se conservan. ¿Aplicar?").setPositiveButton("Aplicar") { _,_ -> guard { val merged=result.mapValues { (id,m) -> store.mark(id).copy(favorite=m.favorite,note=m.note) }; store.restore(merged); status.text="Restaurada en transacción" } }.setNegativeButton("Cancelar",null).show() }
      // Paso del flujo: "Copia validada. Pendiente de confirmación."
      "Copia validada. Pendiente de confirmación."
     // Cierra el bloque anterior y devuelve el control a su contexto.
     }
    // Cierra el bloque anterior y devuelve el control a su contexto.
    }
   // Cierra el bloque anterior y devuelve el control a su contexto.
   }
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento onResume.
 override fun onResume() { super.onResume(); sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { sensors.registerListener(this,it,SensorManager.SENSOR_DELAY_UI) } }
 // Implementa la respuesta al evento onPause.
 override fun onPause() { sensors.unregisterListener(this); stopRecording(false); releaseAudio(); super.onPause() }
 // Implementa la respuesta al evento onSensorChanged.
 override fun onSensorChanged(event: SensorEvent) {
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(!shake || selected!=null || refresh==null) return
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val v=event.values
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val magnitude=kotlin.math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2])
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val now=android.os.SystemClock.elapsedRealtime()
  // Evalúa la condición y ejecuta únicamente el camino correspondiente.
  if(magnitude>22f && now-lastShake>2000) { lastShake=now; guard { E05Sensor.choose(visible,kotlin.random.Random.nextInt())?.let { detail(it) } } }
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Implementa la respuesta al evento onAccuracyChanged.
 override fun onAccuracyChanged(sensor: Sensor?,accuracy: Int) {}
 // Implementa la respuesta al evento onSaveInstanceState.
 override fun onSaveInstanceState(state: Bundle) { state.putString("query",query); state.putString("club",club); state.putBoolean("favorites",onlyFavorites); state.putInt("selected",selected?.id ?: -1); super.onSaveInstanceState(state) }
 // Implementa la respuesta al evento onDestroy.
 override fun onDestroy() { picture?.animate()?.cancel(); link.close(); stopRecording(false); releaseAudio(); worker.shutdown(); super.onDestroy() }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
