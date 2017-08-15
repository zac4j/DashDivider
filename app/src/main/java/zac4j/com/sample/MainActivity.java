package zac4j.com.sample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zac4j.decor.GridDashDivider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView stockListView = (RecyclerView) findViewById(R.id.main_rv_list);

    updateUi(stockListView);
  }

  private void updateUi(RecyclerView stockListView) {
    stockListView.setLayoutManager(new GridLayoutManager(this, 4));
    RecyclerView.ItemDecoration dashDivider = new GridDashDivider.Builder(this).dashGap(5)
        .dashLength(5)
        .dashThickness(3)
        .color(ContextCompat.getColor(this, R.color.colorPrimary))
        .drawer(true, true, true, true)
        .offset(10, 10, 10, 10)
        .build();
    stockListView.addItemDecoration(dashDivider);

    StockListAdapter adapter = new StockListAdapter(this);
    stockListView.setAdapter(adapter);
    adapter.addStockList(fetchData());
  }

  private List<Stock> fetchData() {
    List<Stock> stockList = new ArrayList<>();
    String[] stocks = getResources().getStringArray(R.array.stocks);
    for (String stockInfos : stocks) {
      String[] stockInfo = stockInfos.split(";");
      Stock stock = new Stock(stockInfo[0], stockInfo[1]);
      stockList.add(stock);
    }
    return stockList;
  }

  class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {

    private Context mContext;
    private List<Stock> mStockList;

    StockListAdapter(Context context) {
      this.mContext = context;
      mStockList = new ArrayList<>();
    }

    void addStockList(List<Stock> stockList) {
      if (stockList == null || stockList.isEmpty()) {
        return;
      }
      int startPosition = mStockList.size();
      mStockList.addAll(stockList);
      notifyItemRangeChanged(startPosition, stockList.size());
    }

    @Override public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_main, parent, false);
      return new StockViewHolder(itemView);
    }

    @Override public void onBindViewHolder(StockViewHolder holder, int position) {
      Stock stock = mStockList.get(position);

      if (stock == null) {
        return;
      }

      holder.bindTo(stock);
    }

    @Override public int getItemCount() {
      if (mStockList == null || mStockList.isEmpty()) {
        return 0;
      }
      return mStockList.size();
    }

    class StockViewHolder extends RecyclerView.ViewHolder {

      private TextView mNameView;
      private TextView mCodeView;

      StockViewHolder(View itemView) {
        super(itemView);
        mNameView = itemView.findViewById(R.id.item_main_tv_name);
        mCodeView = itemView.findViewById(R.id.item_main_tv_code);
      }

      void bindTo(Stock stock) {
        if (TextUtils.isEmpty(stock.getName())) {
          String name = getString(R.string.empty_stock_name);
          mNameView.setText(name);
        } else {
          mNameView.setText(stock.getName());
        }

        if (TextUtils.isEmpty(stock.getCode())) {
          String code = getString(R.string.empty_stock_code);
          mNameView.setText(code);
        } else {
          mCodeView.setText(stock.getCode());
        }
      }
    }
  }
}
