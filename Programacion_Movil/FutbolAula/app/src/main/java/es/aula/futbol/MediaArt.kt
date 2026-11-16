// Sitúa el archivo en el paquete común; la carpeta organiza el material didáctico.
package es.aula.futbol
// Importa android.graphics.* para poder utilizar su API.
import android.graphics.*
// Importa java.io.File para poder utilizar su API.
import java.io.File
// Declara el modelo o componente que agrupa estas responsabilidades.
object MediaArt {
 // Define card; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun card(p: Player, title: String = "Mi colección", color: Int = 0): Bitmap {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val b=Bitmap.createBitmap(900,1100,Bitmap.Config.ARGB_8888)
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val c=Canvas(b)
  // Paso del flujo: c.drawColor(listOf(Color.rgb(15,35,58),Color.rgb(70,20,65),Color.rgb(15,65,45))[color.coerceIn(0,2)])
  c.drawColor(listOf(Color.rgb(15,35,58),Color.rgb(70,20,65),Color.rgb(15,65,45))[color.coerceIn(0,2)])
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val paint=Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign=Paint.Align.CENTER; this.color=Color.WHITE }
  // Define line; sus parámetros indican los datos de entrada y el tipo final indica la salida.
  fun line(text: String,y: Float,maxSize: Float) {
   // Paso del flujo: paint.textSize=maxSize
   paint.textSize=maxSize
   // Paso del flujo: while(paint.measureText(text)>820f && paint.textSize>12f) paint.textSize-=1f
   while(paint.measureText(text)>820f && paint.textSize>12f) paint.textSize-=1f
   // Paso del flujo: c.drawText(text,450f,y,paint)
   c.drawText(text,450f,y,paint)
  // Cierra el bloque anterior y devuelve el control a su contexto.
  }
  // Paso del flujo: line(title,120f,45f)
  line(title,120f,45f)
  // Paso del flujo: line(p.name.split(" ").take(2).joinToString(""){it.take(1)},430f,170f)
  line(p.name.split(" ").take(2).joinToString(""){it.take(1)},430f,170f)
  // Paso del flujo: line(p.name,650f,58f)
  line(p.name,650f,58f)
  // Paso del flujo: line(p.club,750f,42f)
  line(p.club,750f,42f)
  // Paso del flujo: line(p.position,830f,36f)
  line(p.position,830f,36f)
  // Paso del flujo: line("Avatar generado, no fotografía",1020f,25f)
  line("Avatar generado, no fotografía",1020f,25f)
  // Devuelve este resultado al código que invocó la función.
  return b
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define gray; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun gray(source: Bitmap): Bitmap = transform(source,ColorMatrix().apply { setSaturation(0f) })
 // Define contrast; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun contrast(source: Bitmap, scale: Float, offset: Float): Bitmap = transform(source,ColorMatrix(floatArrayOf(scale,0f,0f,0f,offset,0f,scale,0f,0f,offset,0f,0f,scale,0f,offset,0f,0f,0f,1f,0f)))
 // Define transform; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 private fun transform(source: Bitmap,matrix: ColorMatrix): Bitmap {
  // Crea una referencia de solo lectura que se utilizará en los siguientes pasos.
  val b=Bitmap.createBitmap(source.width,source.height,Bitmap.Config.ARGB_8888)
  // Paso del flujo: Canvas(b).drawBitmap(source,0f,0f,Paint().apply { colorFilter=ColorMatrixColorFilter(matrix) })
  Canvas(b).drawBitmap(source,0f,0f,Paint().apply { colorFilter=ColorMatrixColorFilter(matrix) })
  // Devuelve este resultado al código que invocó la función.
  return b
 // Cierra el bloque anterior y devuelve el control a su contexto.
 }
 // Define save; sus parámetros indican los datos de entrada y el tipo final indica la salida.
 fun save(bitmap: Bitmap,file: File,format: Bitmap.CompressFormat) { file.outputStream().use { check(bitmap.compress(format,90,it)) } }
// Cierra el bloque anterior y devuelve el control a su contexto.
}
