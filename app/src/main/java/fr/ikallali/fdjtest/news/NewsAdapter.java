package fr.ikallali.fdjtest.news;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.ikallali.fdjtest.R;

import static fr.ikallali.fdjtest.Settings.NEWS_DATE_FORMAT;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    static final String TAG = "NewsAdapter";

    private List<News> newsList;
    private ItemClickListener clickListener;

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public NewsAdapter(List<News> newsList, ItemClickListener listener) {
        this.newsList = newsList;
        this.clickListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news_row, parent, false);

        return new ViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(Html.fromHtml(news.getTitle()));
        holder.date.setText(news.getPubDateStr(NEWS_DATE_FORMAT));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView title,  date;
        private ItemClickListener mListener;

        public ViewHolder(View view, ItemClickListener listener) {
            super(view);

            this.mListener = listener;
            this.title = view.findViewById(R.id.title);
            this.date = view.findViewById(R.id.date);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(mListener != null)
               mListener.onClick(view, getLayoutPosition());
        }
    }


}
