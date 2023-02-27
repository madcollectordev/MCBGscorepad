package fr.free.madcollector.mcbgscorepad.templates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class BGTemplateScoringLineCardAdapter extends RecyclerView.Adapter<BGTemplateScoringLineCardAdapter.MyViewModel>
        implements BGTemplateScoringLineMoveCallback.RecyclerViewRowTouchHelperContract
{

    private List<BGTemplateScoringLine> dataList;
    private FragmentTemplateEdit myFragment;

    public BGTemplateScoringLineCardAdapter(FragmentTemplateEdit parentFragment)
    {
        myFragment = parentFragment;
    }
    public void setDataList(List<BGTemplateScoringLine> dataList){
        this.dataList =  dataList;
    }

    @Override
    public MyViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_bgscoreline_card,parent,false);
        return new MyViewModel(view);
    }

    @Override
    public void onBindViewHolder(MyViewModel holder, int position) {
        holder.scorelineName.setText(dataList.get(position).getName());
        if(dataList.get(position).getIconString()!=null)
        {
            byte[] decodedString = Base64.decode(dataList.get(position).getIconString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.scoreLineIcon.setImageBitmap(decodedByte);
        }
        else holder.scoreLineIcon.setImageBitmap(null);
        if(dataList.get(position).getColor()!=-1)
        {
            holder.scoreLineIcon.getBackground().setTint(dataList.get(position).getColor());
        }
        else holder.scoreLineIcon.getBackground().setTint(ContextCompat.getColor(myFragment.getActivity(), com.google.android.material.R.color.design_default_color_secondary_variant));

        holder.cardView.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("editScorelineTemplate_name", myFragment.getTemplate().getName());
                bundle.putString("editScoreline_name", holder.scorelineName.getText().toString());
                NavHostFragment.findNavController(myFragment).navigate(R.id.action_FragmentTemplateEdit_to_FragmentScorelineEdit, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onRowMoved(int from, int to) {
        if(from < to)
        {
            for(int i=from; i<to; i++)
            {
                Collections.swap(dataList,i,i+1);
            }
        }
        else
        {
            for(int i=from; i>to; i--)
            {
                Collections.swap(dataList,i,i-1);
            }
        }
        notifyItemMoved(from,to);
        ((MainActivity)myFragment.getActivity()).getBgTemplateIO().saveTemplate(myFragment.getTemplate());
    }

    @Override
    public void onRowSelected(MyViewModel myViewHolder) {
        myViewHolder.cardView.setAlpha(new Float(0.7));
    }

    @Override
    public void onRowClear(MyViewModel myViewHolder) {
        myViewHolder.cardView.setAlpha(new Float(1));
    }
    class MyViewModel extends RecyclerView.ViewHolder{

        TextView scorelineName;
        ImageView scoreLineIcon;
        CardView cardView;
        RelativeLayout scoreLineLayout;

        public MyViewModel(View itemView) {
            super(itemView);
            scorelineName = itemView.findViewById(R.id.scorelinelist_name);
            scoreLineIcon = itemView.findViewById(R.id.scoreLineIcon);
            cardView = itemView.findViewById(R.id.scorelineCardView);
            scoreLineLayout = itemView.findViewById(R.id.scoreLineLayout);


        }
    }
}