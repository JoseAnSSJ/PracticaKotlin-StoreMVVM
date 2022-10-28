package com.example.store.EditModule

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.store.MainModule.MainActivity
import com.example.store.R
import com.example.store.Common.Entity.StoreEntity
import com.example.store.MainModule.ViewModel.EditSotreViewModel
import com.example.store.databinding.FragmentEditStoreBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


class EditStoreFragment : Fragment() {

    lateinit var mEditStoreViewMode: EditSotreViewModel
    lateinit var binding: FragmentEditStoreBinding
    private var mActivity: MainActivity? = null
    private var isEditMode: Boolean = false
    private lateinit var storeEntityEdit: StoreEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditStoreViewMode = ViewModelProvider(requireActivity())[EditSotreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditStoreBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpViewModel()


        setUpTextFild()
    }

    private fun setUpViewModel() {

        mEditStoreViewMode.getStoreSelect().observe(viewLifecycleOwner){
            storeEntityEdit = it
            if (it.id != 0L){
                isEditMode=true
                setUiStore(it)
            }else{
                isEditMode = false
            }

            setUpActionBar()
        }

        mEditStoreViewMode.getResult().observe(viewLifecycleOwner){ result ->
            HideKayboard()

            when(result){
                is Long ->{
                    storeEntityEdit.id = result
                    mEditStoreViewMode.setSotoreSelect(storeEntityEdit)

                    Toast.makeText(mActivity,getString(R.string.edit_store_success_save_menssage),Toast.LENGTH_LONG).show()
                    mActivity?.onBackPressed()
                }
                is StoreEntity ->{

                    mEditStoreViewMode.setSotoreSelect(storeEntityEdit)
                    Snackbar.make(binding.root, getString(R.string.edit_store_success_update_menssage), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setUpActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = if(isEditMode) getString(R.string.edit_title_bar) else getString(
            R.string.create_title_bar
        )
        setHasOptionsMenu(true)
    }

    private fun setUpTextFild() {
        with(binding){
            edName.addTextChangedListener {
                validateFilds(tiName)
            }
            edPhone.addTextChangedListener {
                validateFilds(tiPhone)
            }
            photoUrl.addTextChangedListener {
                validateFilds(tiUrl)
                loadImagen(it.toString())
            }
        }
    }

    private fun loadImagen(url: String){
        Glide.with(this).load(url).diskCacheStrategy(
            DiskCacheStrategy.ALL).centerCrop().into(binding.imgPhoto)
    }



    private fun setUiStore(storeEntity: StoreEntity) {
        with(binding){
            edName.text = storeEntity.name.editable()
            edPhone.text = storeEntity.phone.editable()
            edWeb.text = storeEntity.webSite.editable()
            photoUrl.text = storeEntity.photoUrl.editable()
        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            android.R.id.home ->{
                HideKayboard()
                mActivity?.onBackPressed()
                true
            }
            R.id.action_save ->{

                if(validateFilds(binding.tiUrl, binding.tiPhone, binding.tiName)){
                    with(storeEntityEdit){
                        name = binding.edName.text.toString().trim()
                        phone = binding.edPhone.text.toString().trim()
                        photoUrl = binding.photoUrl.text.toString().trim()
                        webSite = binding.edWeb.text.toString().trim()
                    }
                    if(isEditMode){
                        mEditStoreViewMode.updateStore(storeEntityEdit)
                    }else{
                        mEditStoreViewMode.saveStore(storeEntityEdit)
                    }
                }
                true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }

    }

    private fun validateFilds(vararg textFilds: TextInputLayout): Boolean{
        var isValid = true

        for (textFild in textFilds){
            if(textFild.editText?.text.toString().trim().isEmpty()){
                textFild.error = getString(R.string.helper_requied)
                isValid = false
            }else{
                textFild.error = null
            }
        }

        if(!isValid){
            Snackbar.make(binding.root, getString(R.string.valid_required), Snackbar.LENGTH_LONG).show()
        }

        return isValid
    }

    private fun validateFilds(): Boolean {
        var isValid = true

        if(binding.photoUrl.text.toString().trim().isEmpty()){
            binding.tiUrl.error = getString(R.string.helper_requied)
            isValid = false
            binding.photoUrl.requestFocus()
        }

        if(binding.edPhone.text.toString().trim().isEmpty()){
            binding.tiPhone.error = getString(R.string.helper_requied)
            isValid = false
            binding.edPhone.requestFocus()
        }

        if(binding.edName.text.toString().trim().isEmpty()){
            binding.tiName.error = getString(R.string.helper_requied)
            isValid = false
            binding.edName.requestFocus()
        }

        return isValid
    }

    private fun HideKayboard(){
        val imn = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(requireView().windowToken, 0)

    }

    override fun onDestroyView() {
        HideKayboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        setHasOptionsMenu(false)
        mEditStoreViewMode.setResult(Any())
        mEditStoreViewMode.setShowFab(true)
        super.onDestroy()
    }


}