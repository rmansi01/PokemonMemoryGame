package net.azarquiel.pokemonmemorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.core.view.forEach
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_main.*
import net.azarquiel.pokemonmemorygame.model.Ficha
import org.jetbrains.anko.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var ivprimera : ImageView? = null
    var ivsegunda : ImageView? = null
    var isPrimera = true    // bandera que controla si es la primera imagen o la segunda
    var tapando = true // bandera que controla si estamos tapando los pokemon e impide la interaccion del usuario
    var id1 : Int? = null
    var id2 : Int? = null
    var intentos = 0
    var aciertos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeTablero()
        longToast("Los pokemon se taparán en 5 segundos")
        ponTapa()

    }

    private fun makeTablero() {
        for (i in 0 until 2)
            for (j in 0 until 15)
                vector.add(j)
        vector.shuffle()

        var cont = 0
        var id: Int
        for (i in 0 until lv.size) {
            val lh = lv.getChildAt(i) as LinearLayout
            for (j in 0 until lh.size) {
                ivmatriz[i][j] = lh.getChildAt(j) as ImageView
                ivmatriz[i][j]!!.tag = Ficha(i, j, vector[cont])
                id = resources.getIdentifier("pokemon${vector[cont]}", "drawable", packageName)
                ivmatriz[i][j]!!.setBackgroundResource(id)
                ivmatriz[i][j]!!.setOnClickListener(this)
                cont++
            }
        }
    }

    override fun onClick(v: View?) {
        val imagenPulsada = v as ImageView
        val ficha = imagenPulsada.tag as Ficha
        if (tapando) {
            return
        }
        // comprobamos si es la primera imagen
        if (isPrimera){
            ivprimera = imagenPulsada
            ivmatriz[ficha.i][ficha.j]!!.setImageResource(android.R.color.transparent)
            id1 = ficha.pokemon
            isPrimera = false
        } else {
            if(imagenPulsada.equals(ivprimera)){
                toast("No puedes pulsar dos veces la misma carta")
                return
            }
            ivsegunda = imagenPulsada
            ivmatriz[ficha.i][ficha.j]!!.setImageResource(android.R.color.transparent) // quita la tapa
            id2 = ficha.pokemon
            isPrimera = true
            checkImages()
            intentos ++
        }




    }

    var ivmatriz = Array(6) { arrayOfNulls<ImageView>(5) }
    var vector = ArrayList<Int>()

    private fun checkImages(){
        if (id1!! == id2!!){
            ivprimera!!.isEnabled = false
            ivsegunda!!.isEnabled = false
            aciertos ++
            if (aciertos == 15)
                fin()
        } else {
            tapando = true
            tapa()
        }
    }

    private fun fin(){
        alert("Enhorabuena, lo has resuelto en $intentos intentos") {
            positiveButton("YEES!!!"){}
        }.show()

    }



    private fun ponTapa() {
        doAsync {
            SystemClock.sleep(5000)
            uiThread {
                for (i in 0 until lv.size) { // recorremos el linear vertical size 6
                    val lh = lv.getChildAt(i) as LinearLayout
                    for (j in 0 until lh.size) { // recorremos cada Linear Horizontal size 5
                        ivmatriz[i][j]!!.setImageResource(R.drawable.tapa)
                        tapando = false
                    }
                }
            }
        }
    }
        private fun tapa() {

            doAsync {
            SystemClock.sleep(2000)
            uiThread {
                ivprimera!!.setImageResource(R.drawable.tapa)
                ivsegunda!!.setImageResource(R.drawable.tapa)
                tapando = false
            }
        }


    }
}


