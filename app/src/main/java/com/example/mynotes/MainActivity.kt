package com.example.mynotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticcket.view.*


class MainActivity : AppCompatActivity() {

    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         //Trial data

//        listNotes.add(Note(1," meet professor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//        listNotes.add(Note(2," meet doctor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//         listNotes.add(Note(3," meet friend","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))


        //Load from DB


        LoadQuery("%")
        // % is used to get/fetch anything


    }

    override  fun onResume() {
        super.onResume()
        LoadQuery("%")
        Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show()
    }

    fun LoadQuery(title:String){



        var dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projections,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){

            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID,Title,Description))

            }while (cursor.moveToNext())
        }

        var myNotesAdapter= MyNotesAdpater(this, listNotes)
        list.adapter=myNotesAdapter


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.mainmenu, menu)


//        val sm= getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val sv= menu.findItem(R.id.search).actionView as SearchView
//       sv.setSearchableInfo(sm.getSearchableInfo(componentName))
//       sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//           override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
//                LoadQuery("%$query%")
//               return false
//           }
////
//           override fun onQueryTextChange(newText: String): Boolean {
//                return false
//           }
//       })


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId){
                R.id.addnote->{
                    //Got to add paage
                    var intent= Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    inner class  MyNotesAdpater:BaseAdapter{
        var listNotesAdpater=ArrayList<Note>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater:ArrayList<Note>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var myView=layoutInflater.inflate(R.layout.ticcket,null)

            var myNote=listNotesAdpater[p0]
            myView.tvTitle.text=myNote.nodeName
            myView.tvDes.text=myNote.nodeDes
            myView.ivDelete.setOnClickListener{
                var dbManager=DbManager(this.context!!)
                val selectionArgs= arrayOf(myNote.nodeID.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
          }
            myView.ivEdit.setOnClickListener{

                GoToUpdate(myNote)

            }
            return myView
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {

            return listNotesAdpater.size

        }



    }


    fun GoToUpdate(note:Note){
        var intent=  Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.nodeID)
        intent.putExtra("name",note.nodeName)
        intent.putExtra("des",note.nodeDes)
        startActivity(intent)
    }


}
