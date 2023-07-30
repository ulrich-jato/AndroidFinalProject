package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.bearData.BearViewModel;
import algonquin.cst2335.finalproject.bearData.ImageEntity;
import algonquin.cst2335.finalproject.databinding.ActivityBearImageGeneratorBinding;
import algonquin.cst2335.finalproject.databinding.ImageBearBinding;

public class BearImageGenerator extends AppCompatActivity {
    ActivityBearImageGeneratorBinding binding;
    ArrayList<ImageEntity> imageList ;
    private RecyclerView.Adapter myAdapter;
    BearViewModel bearModel;
    RequestQueue queue = null ;
    int imageHeight;
    int imageWidth;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBearImageGeneratorBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());
        bearModel = new ViewModelProvider(this).get(BearViewModel.class);
        imageList = bearModel.imageList.getValue();

        if(imageList == null)
        {
            bearModel.imageList.postValue(imageList = new ArrayList<ImageEntity>());
        }

        binding.generateImageButton.setOnClickListener(click ->{
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
                    int j = 10;
            });

            queue.add(imgReq);
            binding.height.setText("");
            binding.width.setText("");
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
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}