package jesus.net.ejemplorecyclerviewkotlin

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class AdaptadorCustom(var contexto: Context, items: ArrayList<Plato>, var listener: IClickListener, var longClickListener: ILongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items: ArrayList<Plato>? = null
    var multiSeleccion = false
    var itemsSeleccionados: ArrayList<Int>? = null
    var viewHolder: ViewHolder? = null

    init {
        this.items = items
        this.itemsSeleccionados = ArrayList()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items?.get(position)
        viewHolder.foto?.setImageResource(item?.foto!!)
        viewHolder.nombre?.text = item?.nombre
        viewHolder.precio?.text = item?.precio.toString() + "€"
        viewHolder.rating?.rating = item?.rating!!

        if(itemsSeleccionados?.contains(position)!!) {
            viewHolder.vista.setBackgroundColor(Color.LTGRAY)
        } else {
            viewHolder.vista.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {
        val vista = LayoutInflater.from(contexto).inflate(R.layout.template_platos, viewGroup, false)
        viewHolder = ViewHolder(vista, listener, longClickListener)
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    fun iniciarActionMode() {
        multiSeleccion = true
    }

    fun destruirActionMode() {
        multiSeleccion = false
        this.itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode() {
        // Eliminar elemento seleccionado
        for (item in itemsSeleccionados!! ) {
            itemsSeleccionados?.removeAt(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()
    }

    fun seleccionarItem(index: Int) {
        if(multiSeleccion) {
            if(itemsSeleccionados?.contains(index)!!) {
                itemsSeleccionados?.remove(index)
            } else {
                itemsSeleccionados?.add(index)
            }
            notifyDataSetChanged()
        }
    }

    fun obtenerElementosSeleccionados(): Int {
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados() {
        if(itemsSeleccionados?.count()!! > 0) {
            var itemsEliminados = ArrayList<Plato>()
            for (index in itemsSeleccionados!!) {
                itemsEliminados.add(items?.get(index)!!)
            }

            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

    class ViewHolder(vista: View, listener: IClickListener, longClickListener: ILongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener {

        var vista = vista
        var foto: ImageView? = null
        var nombre: TextView? = null
        var precio: TextView? = null
        var rating: RatingBar? = null
        var listener: IClickListener? = null
        var longListener: ILongClickListener? = null

        init {
            this.foto = vista.findViewById(R.id.imageViewFoto)
            this.nombre = vista.findViewById(R.id.textViewNombre)
            this.precio = vista.findViewById(R.id.textViewPrecio)
            this.rating = vista.findViewById(R.id.ratingBar)
            this.listener = listener
            this.longListener = longClickListener
            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            this.listener?.onClick(view!!, adapterPosition)
        }

        override fun onLongClick(view: View?): Boolean {
            this.longListener?.longClick(view!!, adapterPosition)
            return true
        }
    }

}