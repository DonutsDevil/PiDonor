package com.example.pidonor.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pidonor.R
import com.example.pidonor.model.Person

class BloodGroupAdapter(private val context: Context,
                        private var personList: List<Person>,
                        private val onMailClickListener: OnSendMailClickListener)
    : RecyclerView.Adapter<BloodGroupAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_blood_group,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val person = personList[position]
        holder.setName("${person.firstName} ${person.lastName}")
        holder.setBloodGroup(person.bloodGroup!!)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    fun setList(list: List<Person>) {
        personList = list
        notifyDataSetChanged()
    }

    inner class Holder (view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private val tvName: TextView = view.findViewById(R.id.tv_name)
        private val tvBloodGroup: TextView = view.findViewById(R.id.tv_bloodGroup)

        init {
            val btnSendMail: Button = view.findViewById(R.id.btn_sendMail)
            btnSendMail.setOnClickListener(this)
        }

        fun setName(name: String) {
            tvName.text = name
        }

        fun setBloodGroup(type: String) {
            tvBloodGroup.text = "Blood-Group: $type"
        }
        override fun onClick(p0: View?) {
            val person = personList[adapterPosition]
            onMailClickListener.onSendMailClick(person)
        }

    }

    // to handle event when send mail is clicked
    interface OnSendMailClickListener {
        fun onSendMailClick(person:Person)
    }
}