package paulbriseno.ittepi.edu.mx.tpdm_u3_practica2_brisenorosales

import android.app.ProgressDialog
import android.os.AsyncTask
import android.widget.Toast
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.lang.UnsupportedOperationException as UnsupportedOperationException1

class ConexionWeb (p: MainActivity) : AsyncTask<URL, Void, String >() {

    var puntero = p
    var variablesEnvio = ArrayList<String>()
    var dialogo = ProgressDialog(puntero)

    override fun onPreExecute() {
        super.onPreExecute()
        dialogo.setTitle("ATECION!!!")
        dialogo.setMessage("Conectando con el Servidor UWU..")
        dialogo.show()
    }
    fun agregarVariables(clave: String, valor: String){
        var cad = clave + "&" + valor
        variablesEnvio.add(cad)
    }

    override fun doInBackground(vararg params: URL?): String {
        var respuesta = ""
        var cadenaEnvioPOST = ""
        var total = variablesEnvio.size - 1
        (0..total).forEach {
            try {
                var data = variablesEnvio.get(it).split("&")
                cadenaEnvioPOST += data[0] + "=" + URLEncoder.encode(data[1], "utf-8" + " " )

            }catch (err: UnsupportedOperationException1){
                respuesta = "ERROR EN CONDIFICACION URL"
            }
        }
        cadenaEnvioPOST = cadenaEnvioPOST.trim()// adios espacios en blanco
        cadenaEnvioPOST = cadenaEnvioPOST.replace(" ", "&")// cambia caracteres por otros

        var conexion: HttpURLConnection? = null
        try {

            conexion = params[0]?.openConnection() as HttpURLConnection
            conexion?.doOutput = true
            conexion?.setFixedLengthStreamingMode(cadenaEnvioPOST.length)
            conexion?.requestMethod = "POST"
            conexion?.setRequestProperty("Content-Type","application/x-www-form-urlencoded")

            var  salida = BufferedOutputStream(conexion?.outputStream)
            salida.write(cadenaEnvioPOST.toByteArray())
            salida.flush()
            salida.close()
            if (conexion?.responseCode == 200){
                var flujoEntrada = InputStreamReader(conexion?.inputStream, "UTF-8")
                var entrada = BufferedReader(flujoEntrada)

                respuesta = """${entrada.readLine()}"""
                entrada.close()
            }else{
                respuesta = "ERROR " + conexion?.responseCode
            }
        }catch (erro: IOException){
            respuesta = "ERROR EN FLUJO DE ENTRADA O SALIDA"
        }finally {
            if(conexion!=null){
                conexion.disconnect()
            }
        }
        return respuesta

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        dialogo.dismiss()

        puntero.mostrarResultados(result!!)
    }


}