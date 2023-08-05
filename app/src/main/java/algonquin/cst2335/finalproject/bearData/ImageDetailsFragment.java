package algonquin.cst2335.finalproject.bearData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ImageDetailsBinding;

public class ImageDetailsFragment extends Fragment {
    ImageEntity selected;
    ImageDAO imageDAO;
    private boolean isSavedImage;

    public ImageDetailsFragment(ImageEntity  image, boolean isSavedImage)
    {
        this.selected = image;
        this.isSavedImage = isSavedImage;
    }

    public interface OnImageDetailsListener {
        void onImageSaved(ImageEntity image);
        void onImageDeleted(ImageEntity image);
    }

    private OnImageDetailsListener imageDetailsListener;
    public void setOnImageDetailsListener(OnImageDetailsListener listener){
        this.imageDetailsListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ImageDetailsBinding binding = ImageDetailsBinding.inflate(inflater);
        //Save the selected flight details to the Room database
        ImageDatabase db = Room.databaseBuilder(requireContext().getApplicationContext(), ImageDatabase.class, "ImageDatabase").build();
        // Get the FlightDAO from the database.
        imageDAO = db.imageDAO();
        binding.imageViewDetails.setImageBitmap(selected.getImage());
        binding.heightText.setText(selected.getHeight());
        binding.widthText.setText(selected.getWeight());

        if(isSavedImage){
            // Display the details with delete button
            binding.saveDeleteDetailsButton.setText("Delete Image");
            binding.saveDeleteDetailsButton.setOnClickListener(click ->{
                showDeleteImageDialog();
            });
        }else{
            // Display the details with save button
            binding.saveDeleteDetailsButton.setText("Save Flight");
            binding.saveDeleteDetailsButton.setOnClickListener(click ->{
                showSavedImageDialog();
            });
        }

        return binding.getRoot();
    }

    private void showSavedImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you Want to save this Image ?");
        builder.setTitle("Attention!");
        builder.setNegativeButton("No", (dialog, which) ->{

        });

        builder.setPositiveButton("Yes", (dialog, which) ->{
            saveImageDetails();
        });
        builder.create().show();
    }

    private void saveImageDetails(){
        // Check if the selected flight already exists in the database.
        long selectedId = selected.getId();
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            ImageEntity existingImage = imageDAO.getImageById(selectedId);

            if (existingImage != null) {
                // Flight already exists in the database, show a message to the user.
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "This Flight has already been saved!", Snackbar.LENGTH_SHORT).show();
                });
            } else {
                // Selected flight does not exist in the database, proceed with saving.
                long id = imageDAO.insertImage(selected);
                selected.setId(id);

                // Make sure to update the UI components on the main thread.
                requireActivity().runOnUiThread(() -> {
                    if (id != -1) {
                        Snackbar.make(requireView(), "Flight details saved!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", (snackbarClick) -> {
                                    undoSavedImageDetails(selected);
                                })
                                .show();
                    } else {
                        Snackbar.make(requireView(), "Failed to save flight details!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDeleteImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Do you want to delete this Image?");
        builder.setTitle("Attention!");
        builder.setNegativeButton("No", (dialog, which) -> {
            // Code to handle "No" button click
        });

        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteImageDetails();
        });
        builder.create().show();
    }

    private void deleteImageDetails() {
        long selectedId = selected.getId();
        Executor thread1 = Executors.newSingleThreadExecutor();
        thread1.execute(() -> {
            ImageEntity existingImage = imageDAO.getImageById(selectedId);

            if (existingImage == null) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "This Flight does not exist in the database!", Snackbar.LENGTH_SHORT).show();
                });
            } else {
                imageDAO.deleteImage(selected);

                requireActivity().runOnUiThread(() -> {
                    if (imageDetailsListener != null) {
                        imageDetailsListener.onImageDeleted(selected);
                    }
                    Snackbar.make(requireView(), "Flight details deleted!", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", (snackbarClick) -> {
                                undoDeleteddImageDetails(selected);
                            })
                            .show();
                });
            }
        });
    }


    private void undoSavedImageDetails(ImageEntity image){
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();

        // Perform the database operation on the separate thread.
        thread1.execute(() -> {
            imageDAO.deleteImage(image);

            // Make sure to update the UI components on the main thread.
            requireActivity().runOnUiThread(() -> {
                Snackbar.make(requireView(), "Flight details undone!", Snackbar.LENGTH_SHORT).show();
            });

        });
    }

    private void undoDeleteddImageDetails(ImageEntity image){
        // Create an Executor to run the database operation on a separate thread.
        Executor thread1 = Executors.newSingleThreadExecutor();

        // Perform the database operation on the separate thread.
        thread1.execute(() -> {
            imageDAO.insertImage(image);

            // Make sure to update the UI components on the main thread.
            requireActivity().runOnUiThread(() -> {
                if (imageDetailsListener != null) {
                    imageDetailsListener.onImageSaved(selected);
                }
                Snackbar.make(requireView(), "Flight details undone!", Snackbar.LENGTH_SHORT).show();
            });

        });
    }
}
