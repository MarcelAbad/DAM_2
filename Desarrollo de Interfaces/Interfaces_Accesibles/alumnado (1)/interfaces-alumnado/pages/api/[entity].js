// BACKEND (no tocar): una API que lee y escribe data/db.json.
// Direcciones: /api/customers, /api/products, /api/orders
import fs from 'fs';                                   // modulo para leer/escribir ficheros
import path from 'path';                               // modulo para construir rutas de ficheros

const FILE = path.join(process.cwd(), 'data', 'db.json'); // ruta del fichero de datos
const ENTITIES = ['customers', 'products', 'orders'];  // listas permitidas
const load = () => JSON.parse(fs.readFileSync(FILE, 'utf8')); // lee el fichero y lo convierte en objeto
const save = (db) => fs.writeFileSync(FILE, JSON.stringify(db, null, 2)); // guarda el objeto en el fichero

export default function handler(req, res) {            // funcion que atiende cada peticion
  const { entity, id } = req.query;                    // de la direccion: que lista y (opcional) que id
  if (!ENTITIES.includes(entity)) return res.status(404).json({ error: 'Lista desconocida' }); // lista no valida
  const db = load();                                   // cargamos todos los datos
  const list = db[entity];                             // la lista pedida
  if (req.method === 'GET') return res.json(list);     // GET = leer: devolvemos la lista
  if (req.method === 'POST') {                         // POST = crear
    const nuevoId = list.reduce((m, x) => Math.max(m, x.id), 0) + 1; // siguiente id libre
    const item = { ...req.body, id: nuevoId };         // elemento nuevo con su id
    list.push(item);                                   // lo anadimos
    save(db);                                          // guardamos
    return res.status(201).json(item);                 // respondemos con lo creado
  }
  const idx = list.findIndex((x) => x.id == id);       // para PUT/DELETE: posicion del elemento
  if (idx < 0) return res.status(404).json({ error: 'No existe' }); // no encontrado
  if (req.method === 'PUT') {                          // PUT = modificar
    list[idx] = { ...list[idx], ...req.body, id: list[idx].id }; // mezclamos datos nuevos
    save(db);                                          // guardamos
    return res.json(list[idx]);                        // devolvemos el elemento modificado
  }
  if (req.method === 'DELETE') {                       // DELETE = borrar
    list.splice(idx, 1);                               // quitamos el elemento
    save(db);                                          // guardamos
    return res.json({ ok: true });                     // confirmamos
  }
  res.status(405).end();                               // otro metodo: no permitido
}
