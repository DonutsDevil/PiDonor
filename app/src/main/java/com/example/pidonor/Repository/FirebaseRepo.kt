package com.example.pidonor.Repository

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pidonor.Utils
import com.example.pidonor.model.Person
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object FirebaseRepo {
    private val database = Firebase.database
    private val donorsListRef = database.getReference(Utils.DONORS_LIST_NODE)
    private const val TAG = "FirebaseRepo"

    // Add peron to db who opt for donating blood
    fun addValue(person: Person): LiveData<Bundle> {
        val id = getRandomId()!!
        val bundle = Bundle()
        val bundleLiveData =  MutableLiveData<Bundle>()
        donorsListRef.child(id).setValue(person).addOnCompleteListener {
            bundle.putBoolean(Utils.STATUS, it.isSuccessful)
            if (it.isSuccessful.not()) {
                bundle.putString(Utils.ERROR, it.exception?.localizedMessage)
            }
            bundleLiveData.value = bundle
        }
        return bundleLiveData
    }

    private fun getRandomId() : String? {
        return donorsListRef.push().key
    }

    // Get all people from db irrespective of the blood group
    fun getAllBloodGroups() : LiveData<List<Person>> {
        val personListsMLD = MutableLiveData<List<Person>>()
        val personList = mutableListOf<Person>()
        donorsListRef.get().addOnSuccessListener {
            Log.d(TAG, "getAllBloodGroups: ${it.value}")
            for(dsp in it.children) {
                val person = dsp.getValue(Person::class.java)!!
                personList.add(person)
            }
            personListsMLD.value = personList
        }
        Log.d(TAG, "getAllBloodGroups: got all people from db: $personList")
        return personListsMLD
    }
}