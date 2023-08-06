package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algonquin.cst2335.finalproject.bearData.BearViewModel;
import algonquin.cst2335.finalproject.bearData.ImageDAO;
import algonquin.cst2335.finalproject.bearData.ImageDatabase;
import algonquin.cst2335.finalproject.bearData.ImageDetailsFragment;
import algonquin.cst2335.finalproject.bearData.ImageEntity;
import algonquin.cst2335.finalproject.databinding.ActivityBearImageGeneratorBinding;
import algonquin.cst2335.finalproject.databinding.ImageBearBinding;

public class BearImageGenerator extends AppCompatActivity implements ImageDetailsFragment.OnImageDetailsListener {
    ActivityBearImageGeneratorBinding binding;
    ArrayList<ImageEntity> imageList ;
    BearViewModel bearModel;
    RequestQueue queue = null ;
    int imageHeight;
    int imageWidth;
    Bitmap image;
    RecyclerView.Adapter<ImageRowHolder> myAdapter;
    ImageDAO imageDAO;

    BearViewModel imageModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearImageGeneratorBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.bearToolbar);

        //Save the selected flight details to the Room database
        ImageDatabase db = Room.databaseBuilder(getApplicationContext(),
                ImageDatabase.class, "ImageDatabase").build();
        imageDAO = db.imageDAO();

        BearViewModel.selectedImage.observe(this, (newValue) -> {
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            boolean isSavedImage = isImageSavedInDatabase(newValue);
            ImageDetailsFragment imageFragment = new ImageDetailsFragment( newValue, isSavedImage);
            imageFragment.setOnImageDetailsListener(this);
            tx.add(R.id.fragmentLocation, imageFragment);
            tx.replace(R.id.fragmentLocation, imageFragment);
            tx.commit();
            tx.addToBackStack("");
        });

        SharedPreferences prefs = getSharedPreferences("ImageData", Context.MODE_PRIVATE);
        String height =  prefs.getString("height", "");
        String width =  prefs.getString("width", "");
        if (!height.equals("")){
            binding.height.setText(height);
        }
        if (!width.equals("")){
            binding.width.setText(width);
        }
        bearModel = new ViewModelProvider(this).get(BearViewModel.class);
        imageList = bearModel.imageList.getValue();

        if(imageList == null)
        {
            bearModel.imageList.postValue(imageList = new ArrayList<ImageEntity>());
        }

        binding.generateImageButton.setOnClickListener(click ->{
            if(binding.width.getText().toString().equals("") || binding.height.getText().toString().equals("")){
                String message = "Please enter a valid data";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            //Bitmap drawableBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
            imageList.clear();
            String heightString  = binding.height.getText().toString();
            imageHeight = Integer.parseInt(heightString);
            String widthString  = binding.width.getText().toString();
            imageWidth = Integer.parseInt(widthString);
            String imageUrl = "https://placebear.com/"+imageWidth+"/"+imageHeight;
            ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    try{
                        image = bitmap;
                        imageList.add(new ImageEntity(imageHeight, imageWidth, image));
                        myAdapter.notifyDataSetChanged();

                    }catch (Exception e){
                        
                    }

                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                    (error ) -> {

            });

            queue.add(imgReq);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("height", binding.height.getText().toString());
            editor.putString("width", binding.width.getText().toString());
            editor.apply();

            binding.height.setText("");
            binding.width.setText("");
        });

        binding.savedImageButton.setOnClickListener(click ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(BearImageGenerator.this);
            builder.setMessage("Do You Want To View Saved Images ?");
            builder.setTitle("Attention!");
            builder.setNegativeButton("No", (cl, which) -> {
                // Code to handle "No" button click
            });

            builder.setPositiveButton("Yes", (cl, which) -> {
                imageList.clear();
                myAdapter.notifyDataSetChanged();
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() ->
                {
                    imageList.addAll(imageDAO.getAllImages()); //Once you get the data from database
                    runOnUiThread( () ->  myAdapter.notifyDataSetChanged());
                });
            });

            builder.create().show();
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<ImageRowHolder>() {
            @NonNull
            @Override
            public ImageRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ImageBearBinding binding = ImageBearBinding.inflate(getLayoutInflater());
                return new ImageRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull ImageRowHolder holder, int position) {
                ImageEntity image = imageList.get(position);
                holder.imageView.setImageBitmap(image.getImage());
            }

            @Override
            public int getItemCount() {
                return imageList.size();
            }
        });
        // Set layout manager for RecyclerView
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    class ImageRowHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk->{
                int position = getAbsoluteAdapterPosition();
                ImageEntity selected = imageList.get(position);
                imageModel.selectedImage.postValue(selected);

            });
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    // Implement the onFlightDeleted() method from the OnFlightDeletedListener interface
    @Override
    public void onImageDeleted(ImageEntity image) {
        // Remove the deleted flight from the flightlist and update the RecyclerView
        imageList.remove(image);
        myAdapter.notifyDataSetChanged();
    }
    @Override
    public void onImageSaved(ImageEntity image) {
        // Save the deleted flight from the flightlist and update the RecyclerView
        imageList.add(image);
        myAdapter.notifyDataSetChanged();
    }
    private boolean isImageSavedInDatabase(ImageEntity image) {
        long selectedId = image.getId();
        // Use the provided ExecutorService to execute the database query in a separate thread
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executorService.submit(() -> {
            for (ImageEntity savedImage : imageDAO.getAllImages()) {
                if (savedImage.getId() == selectedId) {
                    return true;
                }
            }
            return false;
        });

        // Get the result of the database query (true or false) from the Future
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false; // Return a default value in case of an exception
        } finally {
            // Shutdown the ExecutorService to release resources
            executorService.shutdown();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bear_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.menu_bear_home ) {
            startActivity(new Intent(this, MainActivity.class));
        } else if( item.getItemId() == R.id.menu_bear_help ){
            AlertDialog.Builder builder = new AlertDialog.Builder(BearImageGenerator.this)
                    .setMessage("This is the instruction on how to use Bear Image Generator Application")
                    .setTitle("Instructions!")
                    .setPositiveButton("OK", (cl, which) -> {
                    });
            builder.create().show();
        } else if (item.getItemId() == R.id.menu_bear_aviation) {
            startActivity(new Intent(this, AviationTracker.class));
        }else if (item.getItemId() == R.id.menu_bear_currency) {
            startActivity(new Intent(this, CurrencyGenerator.class));
        }else if (item.getItemId() == R.id.menu_bear_trivia) {
            startActivity(new Intent(this, TriviaQuestion.class));
        }
        return true;
    }
}