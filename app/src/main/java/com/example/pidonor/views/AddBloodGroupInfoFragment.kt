package com.example.pidonor.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.pidonor.Utils
import com.example.pidonor.DatePickerFragment
import com.example.pidonor.R
import com.example.pidonor.model.Person
import com.example.pidonor.viewModel.MatchingBloodGroupSVM
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class AddBloodGroupInfoFragment : Fragment(), DatePickerFragment.OnDateSetListener {

    private lateinit var sexTextField: TextInputLayout
    private lateinit var bloodGroupTextField: TextInputLayout
    private lateinit var dobTextField: TextInputLayout
    private lateinit var emailIdTextField: TextInputLayout

    private lateinit var dobIET: TextInputEditText

    private lateinit var firstNameTextField: TextInputLayout
    private lateinit var lastNameTextField: TextInputLayout

    companion object {
        const val ERROR_MESSAGE = "Required"
        val REMOVE_ERROR_MESSAGE = null
        const val TAG = "AddBloodGroupInfoFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_blood_group_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpSpinners()
        val sharedViewModel : MatchingBloodGroupSVM by activityViewModels()
        emailIdTextField.editText!!.setText(sharedViewModel.getEmailId())
        view.findViewById<Button>(R.id.btnDonate).setOnClickListener{
            Utils.hideKeyBoard(requireActivity())
            if (isFirstNameValid() && isLastNameValid() && isSexSelected()
                && isDOBSelected()&& isBloodGroupSelected() && isEmailValid()) {
                Log.d(TAG, "onViewCreated: form validation done, Submitting form")
                val progressDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.progress_dialog,null,false)
                val progressDialog = Utils.createDialog(requireActivity(),progressDialogView)
                progressDialog.show()
                val person = getPerson()
                sharedViewModel.putValue(person).observe(viewLifecycleOwner, {
                    val isSuccessful = it.getBoolean(Utils.STATUS)
                    Log.d(TAG, "onViewCreated: Adding value to Firebase DB is ${it.getBoolean(Utils.STATUS)}")
                    val snackBar = Snackbar.make(view,"",Snackbar.LENGTH_LONG)
                    if (isSuccessful) {
                        snackBar.setText("Record as been recorded")
                    } else {
                        if (it.getString(Utils.ERROR) != null) {
                            snackBar.setText(it.getString(Utils.ERROR)!!)
                        }else {
                            snackBar.setText("Some Error Occurred, Try again later!")
                        }
                    }
                    progressDialog.cancel()
                    snackBar.show()
                })
            }
        }

    }

    private fun initViews(view: View) {
        sexTextField = view.findViewById(R.id.sexMTL)
        bloodGroupTextField = view.findViewById(R.id.bloodGroupsMTL)
        dobTextField = view.findViewById(R.id.DateMTL)
        emailIdTextField = view.findViewById(R.id.emailMTL)
        dobIET = view.findViewById(R.id.dateTIT)
        dobIET.setOnClickListener {
            val datePicker = DatePickerFragment (this)
            datePicker.show(parentFragmentManager, "datePicker")
        }

        firstNameTextField = view.findViewById(R.id.firstNameMTL)
        lastNameTextField = view.findViewById(R.id.LastNameMTL)
    }
    private fun setUpSpinners() {
        val genders = listOf("Male", "Female")
        val bloodGroups = listOf("A+","A-","B+","B-","O+","O-","AB+","AB-")

        val genderAdapter = ArrayAdapter(requireContext(), R.layout.list_item, genders)
        val bloodGroupAdapter = ArrayAdapter(requireActivity(),R.layout.list_item, bloodGroups)

        (sexTextField.editText as? AutoCompleteTextView)?.setAdapter(genderAdapter)
        (bloodGroupTextField.editText as? AutoCompleteTextView)?.setAdapter(bloodGroupAdapter)
    }

    override fun onDateSet(year: Int, month: Int, day: Int) {
        val date = "$day/$month/$year"
        dobIET.setText(date)

    }

    private fun isFirstNameValid(): Boolean {
        return isTextInputLayoutEmpty(firstNameTextField)
    }

    private fun isLastNameValid() : Boolean {
        return isTextInputLayoutEmpty(lastNameTextField)
    }

    private fun isEmailValid() : Boolean {
        return isTextInputLayoutEmpty(emailIdTextField)
    }

    private fun isSexSelected() : Boolean {
        return isSpinnerSelected(sexTextField)
    }

    private fun isBloodGroupSelected() : Boolean {
        return isSpinnerSelected(bloodGroupTextField)
    }

    private fun isDOBSelected() : Boolean {
        return isTextInputLayoutEmpty(dobTextField)
    }

    private fun getPerson(): Person {
        return Person(firstNameTextField.editText!!.text.toString(),
            lastNameTextField.editText!!.text.toString(),
            (sexTextField.editText as? AutoCompleteTextView)!!.text.toString(),
            dobIET.text.toString(),
            (bloodGroupTextField.editText as? AutoCompleteTextView)!!.text.toString(),
            "Swapnil@email.com")
    }

    /**
     * @return true: If textField's edit text is not empty else false
     * */
    private fun isTextInputLayoutEmpty(textField: TextInputLayout): Boolean {
        if (textField.editText!!.text.toString().trim().isEmpty()) {
            // show error
            setError(textField, ERROR_MESSAGE)
            return false
        }
        setError(textField, REMOVE_ERROR_MESSAGE)
        return true
    }

    /**
     * @return true: If we have selected an option else false
     * */
    private fun isSpinnerSelected(textField: TextInputLayout) : Boolean {
        if ((textField.editText as? AutoCompleteTextView)!!.text.toString().trim().isEmpty()) {
            setError(textField, ERROR_MESSAGE)
            return false
        }
        setError(textField, REMOVE_ERROR_MESSAGE)
        return true
    }

    private fun setError(textField: TextInputLayout, errorMessage: String?) {
        textField.error = errorMessage
    }
}