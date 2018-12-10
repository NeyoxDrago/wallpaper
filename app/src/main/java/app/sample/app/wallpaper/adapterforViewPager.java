package app.sample.app.wallpaper;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class adapterforViewPager extends FragmentStatePagerAdapter {

    private Context c;

    public adapterforViewPager(FragmentManager fm , Context c) {

        super(fm);
        this.c = c;
    }

    @Override
    public Fragment getItem(int i)
    {
        if(i==0)
            return fragmentone.getinstance();
        else if (i==1)
            return dailyfragment.getinstance();
        else if (i==2)
            return recentfragment.getinstance(c);
        else
            return  null;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
                return "Wallpapers";

            case 1:
                return "Trending";

            case 2:
                return "Liked";

        }
        return "";
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

}
