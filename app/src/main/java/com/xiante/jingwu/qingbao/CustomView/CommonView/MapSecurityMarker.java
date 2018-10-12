package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.Marker;
import com.xiante.jingwu.qingbao.Bean.Common.SecurityEntity;
import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/11.
 */

public class MapSecurityMarker implements AMap.InfoWindowAdapter {

    private Context context;
    private View infoWindow;
    private TextView nameview,phoneview;

    public MapSecurityMarker(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
            infoWindow = LayoutInflater.from(context).inflate(
                    R.layout.map_security_marker_layout, null);
            nameview=infoWindow.findViewById(R.id.marker_titleTV);
            phoneview=infoWindow.findViewById(R.id.marker_phoneTV);
            infoWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SecurityEntity entity= (SecurityEntity) marker.getObject();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + entity.getStrTel());
                    intent.setData(data);
                    context.startActivity(intent);
                }
            });
        render(marker);
        return infoWindow;
    }

    private void render(Marker marker) {
        SecurityEntity securityEntity= (SecurityEntity) marker.getObject();
        nameview.setText(securityEntity.getStrUnitName());
        phoneview.setText(securityEntity.getStrTel());
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
