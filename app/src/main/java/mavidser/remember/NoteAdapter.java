package mavidser.remember;

/**
 * Created by sid on 14/1/15.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NoteInfo> NoteList;

    public NoteAdapter(List<NoteInfo> NoteList) {
        this.NoteList = NoteList;
    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    @Override
    public void onBindViewHolder(NoteViewHolder NoteViewHolder, int i) {
        NoteInfo ci = NoteList.get(i);
        NoteViewHolder.vTitle.setText(ci.content);
        NoteViewHolder.vDate.setText(ci.date);
        if(ci.pinned.equals("1"))
            NoteViewHolder.vPinned.setVisibility(View.VISIBLE);
        else
            NoteViewHolder.vPinned.setVisibility(View.INVISIBLE);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.notes_list_item, viewGroup, false);

        return new NoteViewHolder(itemView);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vDate;
        protected LinearLayout vPinned;

        public NoteViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.summary);
            vDate = (TextView) v.findViewById(R.id.date);
            vPinned = (LinearLayout) v.findViewById(R.id.pinned_icon_container);
        }
    }


    public void refill(List<NoteInfo> notelist) {
        NoteList.clear();
        NoteList.addAll(notelist);
        notifyDataSetChanged();
    }
}