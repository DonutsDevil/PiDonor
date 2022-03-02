package com.example.pidonor.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pidonor.R
import com.example.pidonor.Utils
import com.example.pidonor.adapter.BloodGroupAdapter
import com.example.pidonor.model.Person
import com.example.pidonor.viewModel.MatchingBloodGroupSVM
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.ImageView


class MatchingBloodGroupFragment : Fragment(), BloodGroupAdapter.OnSendMailClickListener {

    private val TAG = "MatchingBloodGroupFragment"
    private lateinit var progressDialog: AlertDialog
    private var selectedType = "ALL"// default show all
    private lateinit var bloodGroupAdapter: BloodGroupAdapter
    private lateinit var personList: List<Person>
    private lateinit var noDataFoundIv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matching_blood_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel : MatchingBloodGroupSVM by activityViewModels()
        progressDialog = Utils.createDialog(requireActivity(),"Loading...")
        progressDialog.show()
        noDataFoundIv = view.findViewById(R.id.iv_noDataFound)
        sharedViewModel.getALlBloodGroupPeople().observe(viewLifecycleOwner, { personList ->
            if (personList != null && personList.isNotEmpty()) {
                this.personList = personList
                loadBloodGroups(view, personList)
                setNoDataFound(View.GONE)
            }else {
                setNoDataFound(View.VISIBLE)
            }
        })
        view.findViewById<FloatingActionButton>(R.id.fab_addBloodGroup)
            .setOnClickListener {
                it.findNavController().navigate(R.id.action_matchingBloodGroupFragment_to_addBloodGroupInfoFragment)
            }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.blood_group_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        item.isChecked = true
        when(id) {
            R.id.menuShowALl ->selectedType = "ALL"
            R.id.menuA_pve  -> selectedType = "A+"
            R.id.menuA_ng   -> selectedType = "A-"
            R.id.menuB_pve  -> selectedType = "B+"
            R.id.menuB_ng   -> selectedType = "B-"
            R.id.menuO_pve  -> selectedType = "O+"
            R.id.menuO_ng   -> selectedType = "O-"
            R.id.menuAB_pve -> selectedType = "AB+"
            R.id.menuAB_ng  -> selectedType = "AB-"
        }
        Log.d(TAG, "onOptionsItemSelected: selectedType = $selectedType")
        if (selectedType != "ALL") {
            val filteredList = Utils.getSameBloodGroupPerson(selectedType,personList)
            if (filteredList.isNotEmpty()) {
                setNoDataFound(View.GONE)
            } else {
                setNoDataFound(View.VISIBLE)
            }
            Log.d(TAG, "onOptionsItemSelected: filter list = $filteredList")
            bloodGroupAdapter.setList(filteredList)
        } else {
            if (personList.isNotEmpty()) {
                setNoDataFound(View.GONE)
            } else {
                setNoDataFound(View.VISIBLE)
            }
            bloodGroupAdapter.setList(personList)
            Log.d(TAG, "onOptionsItemSelected: selected ALL")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadBloodGroups(view: View, personList: List<Person>) {
        val recyclerView:RecyclerView = view.findViewById(R.id.rv_bloodGroups)
        var personLists:List<Person> = personList
        if(selectedType != "ALL") {
            personLists = Utils.getSameBloodGroupPerson(selectedType,personList)
        }
        bloodGroupAdapter = BloodGroupAdapter(requireContext(),personList,this)
        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = bloodGroupAdapter
        progressDialog.cancel()
    }

    override fun onSendMailClick(person: Person) {
        Log.d(TAG, "onSendMailClick: clicked")
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(person.email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg. Donation of blood")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear sir,\n Hope you are doing well, I am in need and saw you post on PiDonor. I am need of blood")
        emailIntent.selector = selectorIntent

        activity!!.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun setNoDataFound(status: Int) {
        noDataFoundIv.visibility = status
    }
}