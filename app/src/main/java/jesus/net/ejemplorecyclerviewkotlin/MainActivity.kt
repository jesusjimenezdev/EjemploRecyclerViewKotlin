package jesus.net.ejemplorecyclerviewkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var lista: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adaptador: AdaptadorCustom? = null
    var isActionMode = false
    var actionMode: android.support.v7.view.ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var platos = ArrayList<Plato>()
        platos.add(Plato("Plato 1", 250.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 2", 260.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 3", 270.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 4", 280.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 5", 290.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 6", 210.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 7", 220.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 8", 230.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 9", 240.0, 2.8F, R.drawable.plato))
        platos.add(Plato("Plato 10", 240.0, 2.8F, R.drawable.plato))

        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager = layoutManager

        var callBack = object : android.support.v7.view.ActionMode.Callback {

            override fun onActionItemClicked(actionMode: android.support.v7.view.ActionMode?, menuItem: MenuItem?): Boolean {
                when(menuItem?.itemId) {
                    R.id.itemEliminar -> {
                        adaptador?.eliminarSeleccionados()
                    }
                    else -> {
                        return true
                    }
                }
                adaptador?.terminarActionMode()
                actionMode?.finish()
                isActionMode = false
                return true
            }

            override fun onCreateActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                // Inicializar action mode
                adaptador?.iniciarActionMode()
                actionMode = mode
                // inflar menu
                menuInflater.inflate(R.menu.menu_contextual, menu)
                return true
            }

            override fun onPrepareActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                mode?.title = "0 seleccionados"
                return false
            }

            override fun onDestroyActionMode(mode: android.support.v7.view.ActionMode?) {
                adaptador?.destruirActionMode()
                isActionMode = false
            }

        }

        adaptador = AdaptadorCustom(this, platos, object : IClickListener {

            override fun onClick(vista: View, index: Int) {
                Toast.makeText(this@MainActivity, platos.get(index).nombre, Toast.LENGTH_SHORT).show()
            }
        }, object : ILongClickListener {

            override fun longClick(vista: View, index: Int) {
                if (!isActionMode) {
                    startSupportActionMode(callBack)
                    isActionMode = true
                    adaptador?.seleccionarItem(index)
                } else {
                    // hacer selecciones o deselecciones
                    adaptador?.seleccionarItem(index)
                }
                actionMode?.title = adaptador?.obtenerElementosSeleccionados().toString() + "Seleccionados"
            }
        })

        lista?.adapter = adaptador

        // La variable de control de tipo SwipeRefreshLayout no se puede declarar como variable global, al principio de la clase
        val swipe = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
        swipe.setOnRefreshListener {
            for(i in 1..1000000000) {

            }
            swipe.isRefreshing = false
            platos.add(Plato("Plato 10", 240.0, 2.8F, R.drawable.plato))
            adaptador?.notifyDataSetChanged()
        }
    }
}
