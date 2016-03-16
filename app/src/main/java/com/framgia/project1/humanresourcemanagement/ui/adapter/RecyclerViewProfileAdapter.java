package com.framgia.project1.humanresourcemanagement.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.framgia.project1.humanresourcemanagement.R;
import com.framgia.project1.humanresourcemanagement.data.model.Constant;
import java.util.List;

/**
 * Created by hacks_000 on 3/14/2016.
 */
public class RecyclerViewProfileAdapter extends RecyclerView.Adapter<RecyclerViewProfileAdapter.ProfileViewHolder> {

    private List contents;

    public RecyclerViewProfileAdapter(List contents) {
        this.contents = contents;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        ProfileViewHolder profileViewHolder = new ProfileViewHolder(view);
        return profileViewHolder;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.title.setText(Constant.titles[position]);
        holder.content.setText(contents.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return Constant.NUMBER_OF_ITEM_PROFILE;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        public ProfileViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
