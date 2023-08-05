package algonquin.cst2335.finalproject.Currency;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.CurrencySavedBinding;


public class CurrencyDetailsFragment extends Fragment {

    private CurrencyObject selected;

    public CurrencyDetailsFragment() {
        // Required empty public constructor
    }

    public CurrencyDetailsFragment(CurrencyObject m) {
        selected = m;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CurrencySavedBinding binding = CurrencySavedBinding.inflate(inflater);


        binding.converttotext.setText(selected.converto);
        binding.converttext.setText(selected.convertfrom);
        binding.amountf.setText(selected.cfrom);
        binding.amountt.setText(selected.too);
//        binding.database.setText("Id = " + selected.id);

//        binding.delete.setOnClickListener(click -> {
//            deleteConversionFromDatabase();
//        });

        binding.button.setOnClickListener(v -> saveConversionToDatabase());
        return binding.getRoot();
    }

    private void saveConversionToDatabase() {
        CurrencyObject newConversion = new CurrencyObject(selected.convertfrom, selected.converto, selected.cfrom, selected.too);
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(new Runnable() {
            @Override
            public void run() {
                CurrencyDao myDAO = CurrencyDatabase.getInstance(requireContext()).cmDAO();
                List<CurrencyObject> allConversions = myDAO.getMessages();
                for (CurrencyObject conversion : allConversions) {
                    if (conversion.getToo().equals(newConversion.getToo())) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireContext(), "Conversion is already saved.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                long insertedId = myDAO.insertConvertTo(newConversion);
                if (insertedId != -1) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Conversion saved!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Failed to save conversion", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
//    private void deleteConversionFromDatabase() {
//
//        if (selected != null) {
//            Executor thread = Executors.newSingleThreadExecutor();
//            thread.execute(() -> {
//                CurrencyDao myDAO = CurrencyDatabase.getInstance(requireContext()).cmDAO();
//                myDAO.delete(selected);
//
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(requireContext(), "Conversion deleted!", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            });
//        }
//    }
private void deleteConversionFromDatabase () {
//
    if (selected != null) {
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()-> {
            CurrencyDao myDAO = CurrencyDatabase.getInstance(requireContext()).cmDAO();
            myDAO.delete(selected);
            requireActivity().runOnUiThread(()-> {
                        Toast.makeText(requireContext(), "Conversion deleted!",Toast.LENGTH_SHORT).show();
            });
        });
}
}
}
//    Executor thread = Executors.newSingleThreadExecutor();
//    thread.execute(new Runnable() {
//        @Override
//            try {
//                // Your delete operation
//                CurrencyDao myDAO = CurrencyDatabase.getInstance(requireContext()).cmDAO();
//                myDAO.delete(selected);
//
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(requireContext(), "Conversion deleted!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//                requireActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(requireContext(), "Error deleting conversion!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//    });
//}
//
//}