
package com.pax.demo.modules.customerdisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

import java.util.Arrays;

public class WLCustomerDisplayFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private LinearLayout screenLayout;
    private GridView consoleGridView;
    private BackListAdapter adapter = null;
    TextView textView;
    WLCustomerDisPlayTester wlCustomerDisPlayTester;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        screenLayout = (LinearLayout) view.findViewById(R.id.fragment_screen);
        consoleGridView = (GridView) view.findViewById(R.id.fragment_gridview);

        textView = new TextView(getActivity());
        screenLayout.addView(textView);

        wlCustomerDisPlayTester = WLCustomerDisPlayTester.getInstance(getActivity(),textView);
        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.WLcustomerdisplay)), getActivity());
        consoleGridView.setAdapter(adapter);
        consoleGridView.setOnItemClickListener(this);
        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.setPos(i);
        adapter.notifyDataSetChanged();
        switch (i) {
            case 0:
                wlCustomerDisPlayTester.scrInit();
                break;
            case 1:
                wlCustomerDisPlayTester.scrAttrSet();
                break;
            case 2:
                wlCustomerDisPlayTester.scrBackLight();
                break;
            case 3:
                wlCustomerDisPlayTester.scrClrLine();
                break;
            case 4:
                wlCustomerDisPlayTester.scrClrRect();
                break;
            case 5:
                wlCustomerDisPlayTester.scrCls();
                break;
            case 6:
                wlCustomerDisPlayTester.scrDrawRect();
                break;
            case 7:
                wlCustomerDisPlayTester.scrDrLogo();
                break;
            case 8:
                wlCustomerDisPlayTester.scrDrLogoEx();
                break;
            case 9:
                wlCustomerDisPlayTester.scrGetLcdSize();
                break;
            case 10:
                wlCustomerDisPlayTester.scrGetxyEx();
                break;
            case 11:
                wlCustomerDisPlayTester.scrGotoxy();
                break;
            case 12:
                wlCustomerDisPlayTester.scrGotoxyEx();
                break;
            case 13:
                wlCustomerDisPlayTester.scrGray();
                break;
            case 14:
                wlCustomerDisPlayTester.scrLcdDisplay();
                break;
            case 15:
                wlCustomerDisPlayTester.scrPlot();
                break;
            case 16:
                wlCustomerDisPlayTester.scrRestore();
                break;
          /*  case 17:
                wlCustomerDisPlayTester.scrSelectFont();
                break;*/
            case 17:
                wlCustomerDisPlayTester.scrSpaceSet();
                break;
            case 18:
                wlCustomerDisPlayTester.scrUninit();
                break;
            default:
                break;
        }
    }
}
