package com.example.familysafety

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.nfc.cardemulation.CardEmulation
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.familysafety.databinding.FragmentGaurdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GaurdFragment : Fragment(), InviteMailAdapter.OnActionClick {


    lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var binding: FragmentGaurdBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGaurdBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val pinkcard : CardView = view.findViewById(R.id.pink_card)
        pinkcard.setOnClickListener{
            //police activity
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("9100")//replaced by emergency number of that locality
            startActivity(intent)
        }
        val greencard : CardView = view.findViewById(R.id.green_card)
        greencard.setOnClickListener{
            //police activity
            val intent = Intent(Intent.ACTION_CALL);
            intent.data = Uri.parse("9100")//replaced by emergency  medical number of that locality
            startActivity(intent)
        }

        binding.sendInvite.setOnClickListener {
            sendInvite()
        }
        getInvites()
    }

    private fun getInvites() {

        val firestore = Firebase.firestore
        firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .collection("invites").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val list: ArrayList<String> = ArrayList()
                    for (item in it.result) {
                        if (item.get("invite_status") == 0L) {
                            list.add(item.id)
                        }
                    }

                    Log.d("invite89", "getInvites: $list")

                    val adapter = InviteMailAdapter(list,this)
                    binding.inviteRecycler.adapter = adapter

                }
            }


    }

    private fun sendInvite() {
        val mail = binding.inviteMail.text.toString()

        Log.d("Mail89", "sendInvite: $mail")

        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to 0
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(mail)
            .collection("invites")
            .document(senderMail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }

    }

    companion object {

        @JvmStatic
        fun newInstance() = GaurdFragment()
    }

    override fun onAcceptClick(mail: String) {
        Log.d("invite89", "onAcceptClick: $mail")

        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to 1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    override fun onDenyClick(mail: String) {
        Log.d("invite89", "onDenyClick: $mail")


        val firestore = Firebase.firestore

        val data = hashMapOf(
            "invite_status" to -1
        )

        val senderMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        firestore.collection("users")
            .document(senderMail)
            .collection("invites")
            .document(mail).set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}