package com.example.feastbeast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by davidhong12 on 2015-01-01.
 */
public class CreateRecipe extends Activity {


    public List<Recipe> recipes = new ArrayList<Recipe>();
    private Toast mToast;


    EditText getRecipe;
    EditText getIngredients;
    EditText getTitle;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //CHANGE ACTIONBAR COLORS
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView titleText = (TextView)findViewById(titleId);
        titleText.setTextColor(Color.parseColor("#000000"));
        getActionBar().setIcon(android.R.color.transparent);

        //GET BOOKMARKED
        Gson gs = new Gson();
        int i = 0;
        while(getIntent().getStringExtra("bookmarked"+i) != null) {
            String gson = getIntent().getStringExtra("bookmarked"+i);
            recipes.add(gs.fromJson(gson, Recipe.class));
            i++;
        }

        getRecipe = (EditText) findViewById(R.id.getRecipe);
        getIngredients = (EditText) findViewById(R.id.getIngredients);
        getTitle = (EditText) findViewById(R.id.getTitle);

        //SWITCHING
        final Button recipesBtn = (Button) findViewById(R.id.recipeBtn);
        final Button ingredientsBtn = (Button) findViewById(R.id.ingredientsBtn);
        recipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecipe.setVisibility(View.VISIBLE);
                getIngredients.setVisibility(View.INVISIBLE);
                recipesBtn.setBackgroundColor(Color.parseColor("#44b9cb"));
                ingredientsBtn.setBackgroundColor(Color.parseColor("#2eaac0"));
            }
        });
        ingredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecipe.setVisibility(View.INVISIBLE);
                getIngredients.setVisibility(View.VISIBLE);
                recipesBtn.setBackgroundColor(Color.parseColor("#2eaac0"));
                ingredientsBtn.setBackgroundColor(Color.parseColor("#44b9cb"));
            }
        });

        // NAV MENU
        final Button newRecipe = (Button) findViewById(R.id.newRecip);
        final Button create = (Button) findViewById(R.id.create);
        final Button bookmarks = (Button) findViewById(R.id.bookmarks);

        newRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainAct = new Intent(CreateRecipe.this, MainActivity.class);
                Gson gs2 = new Gson();
                String bookmarkss;
                mainAct.putExtra("opened", true);

                List<String> temp = new ArrayList<String>();
                String[] lines = getRecipe.getText().toString().split("\\r?\\n");
                temp = Arrays.asList(lines);

                List<String> temp2 = new ArrayList<String>();
                String[] lines2 = getIngredients.getText().toString().split("\\r?\\n");
                temp2 = Arrays.asList(lines2);

                recipes.add(new Recipe(getTitle.getText().toString(), temp2, temp));

                for(int j = 0; j<recipes.size();j++){
                    bookmarkss = gs2.toJson(recipes.get(j));
                    mainAct.putExtra("bookmarked"+j, bookmarkss);
                }
                startActivity(mainAct);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent createAct = new Intent(CreateRecipe.this,CreateRecipe.class);
                Gson gs2 = new Gson();
                String bookmarkss;

                List<String> temp = new ArrayList<String>();
                String[] lines = getRecipe.getText().toString().split("\\r?\\n");
                temp = Arrays.asList(lines);

                List<String> temp2 = new ArrayList<String>();
                String[] lines2 = getIngredients.getText().toString().split("\\r?\\n");
                temp2 = Arrays.asList(lines2);

                recipes.add(new Recipe(getTitle.getText().toString(), temp2, temp));

                for(int j = 0; j<recipes.size();j++){
                    bookmarkss = gs2.toJson(recipes.get(j));
                    createAct.putExtra("bookmarked" + j, bookmarkss);
                }

                if(!getTitle.getText().toString().isEmpty())
                    createAct.putExtra("title", getTitle.getText().toString());

                startActivity(createAct);
            }
        });
        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent bookmarkAct = new Intent(CreateRecipe.this, ListViewRemovalAnimation.class);
                Gson gs2 = new Gson();
                String bookmarkss;

                List<String> temp = new ArrayList<String>();
                String[] lines = getRecipe.getText().toString().split("\\r?\\n");
                temp = Arrays.asList(lines);

                List<String> temp2 = new ArrayList<String>();
                String[] lines2 = getIngredients.getText().toString().split("\\r?\\n");
                temp2 = Arrays.asList(lines2);

                recipes.add(new Recipe(getTitle.getText().toString(), temp2, temp));

                for(int j = 0; j<recipes.size();j++){
                    bookmarkss = gs2.toJson(recipes.get(j));
                    bookmarkAct.putExtra("bookmarked" + j, bookmarkss);
                }
                startActivity(bookmarkAct);
            }
        });

        //IF EDITING
        if(getIntent().getStringExtra("title") != null){
            getTitle.setText(getIntent().getStringExtra("title"));

            String holder = "";

            List<String> listHolder = new ArrayList<String>();
            listHolder = Arrays.asList(getIntent().getExtras().getStringArray("ingredients"));
            for(int j = 0; j < listHolder.size(); j++){
                holder = holder + listHolder.get(j);
                if(j != listHolder.size() - 1)
                    holder = holder + "\n";
            }
            getIngredients.setText(holder);

            holder = "";

            listHolder = Arrays.asList(getIntent().getExtras().getStringArray("directions"));
            for(int j = 0; j < listHolder.size(); j++){
                holder = holder + listHolder.get(j);
                if(j != listHolder.size() - 1)
                    holder = holder + "\n";
            }
            getRecipe.setText(holder);
        }
    }

    @Override
    protected void onStop(){
        try {
            FileOutputStream output = getApplicationContext().openFileOutput("data", Context.MODE_PRIVATE);
            writeJsonStream(output, recipes);
            output.close();
        }
        catch(Exception e){
        }

        super.onStop();
    }

    @Override
    public void onDestroy(){
        List<String> temp = new ArrayList<String>();
        String[] lines = getRecipe.getText().toString().split("\\r?\\n");
        temp = Arrays.asList(lines);

        List<String> temp2 = new ArrayList<String>();
        String[] lines2 = getIngredients.getText().toString().split("\\r?\\n");
        temp2 = Arrays.asList(lines2);

        recipes.add(new Recipe(getTitle.getText().toString(), temp2, temp));

        try {
            FileOutputStream output = getApplicationContext().openFileOutput("data", Context.MODE_PRIVATE);
            writeJsonStream(output, recipes);
            output.close();
        }
        catch(Exception e){
        }

        super.onDestroy();
    }

    public void writeJsonStream(OutputStream out, List<Recipe> messages) throws IOException {
        Gson gs = new Gson();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        for (Recipe message : messages) {
            gs.toJson(message, Recipe.class, writer);
        }
        writer.endArray();
        writer.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.add:
                if(getTitle.getText().toString().isEmpty()){
                    showToast("Error enter a recipe title");
                }
                else if(getRecipe.getText().toString().isEmpty()){
                    showToast("Error enter recipe directions");
                }
                else if(getIngredients.getText().toString().isEmpty()){
                    showToast("Error enter ingredients");
                }
                else{
                    List<String> temp = new ArrayList<String>();
                    String[] lines = getRecipe.getText().toString().split("\\r?\\n");
                    temp = Arrays.asList(lines);

                    List<String> temp2 = new ArrayList<String>();
                    String[] lines2 = getIngredients.getText().toString().split("\\r?\\n");
                    temp2 = Arrays.asList(lines2);

                    recipes.add(new Recipe(getTitle.getText().toString(), temp2, temp));
                }
                // START BOOKMARKED ACTIVITY
                final Intent bookmarkAct = new Intent(CreateRecipe.this, ListViewRemovalAnimation.class);
                Gson gs2 = new Gson();
                String bookmarkss;
                for(int j = 0; j<recipes.size();j++){
                    bookmarkss = gs2.toJson(recipes.get(j));
                    bookmarkAct.putExtra("bookmarked" + j, bookmarkss);
                }
                startActivity(bookmarkAct);
                break;
            case R.id.help:
                //START ACTIVITY & SEND DATA
                final Intent help = new Intent(CreateRecipe.this, help.class);
                Gson gs3 = new Gson();
                String bookmarksss;
                for(int j = 0; j<recipes.size();j++){
                    bookmarksss = gs3.toJson(recipes.get(j));
                    help.putExtra("bookmarked" + j, bookmarksss);
                }
                startActivity(help);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}
