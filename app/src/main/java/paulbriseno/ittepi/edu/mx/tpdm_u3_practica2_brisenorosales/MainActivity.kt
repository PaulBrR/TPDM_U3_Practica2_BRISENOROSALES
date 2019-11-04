package paulbriseno.ittepi.edu.mx.tpdm_u3_practica2_brisenorosales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    var descripcion : EditText?=null
    var monto : EditText?= null
    var fechavence : EditText?= null
    var pagado : EditText?= null

    var insertar : Button?= null
    var cargar : Button?= null
    var mostrar : Button?= null

    var etiqueta : TextView?= null
    var jsonRegreso = ArrayList<org.json.JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        descripcion=findViewById(R.id.descripcion)
        monto=findViewById(R.id.monto)
        pagado=findViewById(R.id.pagado)
        fechavence=findViewById(R.id.vencimiento)

        insertar=findViewById(R.id.insertar)
        cargar=findViewById(R.id.cargar)
        mostrar=findViewById(R.id.mostrar)
        etiqueta=findViewById(R.id.respuetaText)


        insertar?.setOnClickListener {
            var conexionWeb = ConexionWeb(this)
            conexionWeb.agregarVariables("DESCRIPCION", descripcion?.text.toString()+"\n")
            conexionWeb.agregarVariables("MONTO", monto?.text.toString()+"\n")
            conexionWeb.agregarVariables("FECHAVENCIMIENTO", vencimiento?.text.toString()+"\n")
            conexionWeb.agregarVariables("PAGADO", pagado?.text.toString()+"\n")

            try {
                conexionWeb.execute((URL("https://protected-everglades-89990.herokuapp.com/insertarU3.php")))
            } catch (e: Exception){
                Toast.makeText(this,"TODO BIEN TODO CORRECTO !!!", Toast.LENGTH_LONG)

            }finally {
                Toast.makeText(this,"ALGO DEBE ESTAR PASANDO !!!", Toast.LENGTH_LONG)
            }

            //Metodo de ejecucion en segundo plano del AnsyTask

        }
        cargar?.setOnClickListener {
            var conexionWeb = ConexionWeb(this)
            conexionWeb.execute((URL("https://protected-everglades-89990.herokuapp.com/consultagenericaU3.php")))
        }
        mostrar?.setOnClickListener {
            val posicion = descripcion?.text.toString().toInt()
            val jsonObject = jsonRegreso.get(posicion)

            try {
                etiqueta?.setText("ID: " +jsonObject.getString("ID") + "Descripcion: " + jsonObject.getString("DESCRIPCION") + "Monto: " + jsonObject.getString("MONTO") +
                    "Fecha De Vencimiento: " + jsonObject.getString("FECHAVENCIMIENTO") + "Pagado: " + jsonObject.getString("PAGADO") + "\n \n \n"   )


            }catch (err:Exception){
                Toast.makeText(this,"ERROR : QUIZAS Sin VALORES ", Toast.LENGTH_LONG)
            }



        }

    }
    fun mostrarResultados(result: String) {

        val jsonarray = org.json.JSONArray(result)
        var total = jsonarray.length() - 1
        (0..total).forEach {
            jsonRegreso.add(jsonarray.getJSONObject(it))
        }
        etiqueta?.setText(result)
    }
}
