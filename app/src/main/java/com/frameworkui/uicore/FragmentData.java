package com.frameworkui.uicore;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.frameworkui.ChatFragment;
import com.frameworkui.GalleryFragment;
import com.frameworkui.MainApplication;
import com.frameworkui.MainFragment;
import com.frameworkui.PasscodeFragment;
import com.frameworkui.ProfileFragment;
import com.frameworkui.SearchFragment;

/**
 * Created by ThoLH on 10/7/15.
 */
public class FragmentData {

    public enum FragmentType {
        MAIN(1, MainFragment.class),
        CHAT(2, ChatFragment.class),
        PROFILE(3, ProfileFragment.class),
        GALLERY(4, GalleryFragment.class),
        SEARCH(5, SearchFragment.class),
        PASSCODE(6, PasscodeFragment.class);

        private int typeId;
        private Class<? extends BaseFragment> cls;

        FragmentType(int typeId, Class<? extends BaseFragment> cls) {
            this.typeId = typeId;
            this.cls = cls;
        }

        public int getTypeId() {
            return typeId;
        }

        public Class<? extends BaseFragment> getFragmentClass() {
            return cls;
        }

        public static FragmentType getFragmentTypeById(int typeId) {
            for (FragmentType typeEnum : FragmentType.values()) {
                if (typeEnum.getTypeId() == typeId) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public static class FragmentItem implements Parcelable {

        public final Class<? extends BaseFragment> cls;
        public BaseFragment mFragment;
        public final Bundle mData = new Bundle();
        public int mRequestCode = -1;
        public final FragmentType mFragmentType;

        private int mAnimationEnter, mAnimationExit;

        public FragmentItem(Parcel in) {
            Object obj = in.readSerializable();
            if (obj instanceof Class<?>) {
                cls = (Class<? extends BaseFragment>) obj;
            } else {
                cls = null;
            }
            Bundle dataTemp = in.readBundle(MainApplication.getInstance().getClassLoader());
            if (dataTemp != null) mData.putAll(dataTemp);
            mFragment = null;
            mRequestCode = in.readInt();
            mFragmentType = (FragmentType) in.readSerializable();
            mAnimationEnter = in.readInt();
            mAnimationExit = in.readInt();
        }

        public FragmentItem(FragmentType fragmentType, Bundle data) {
            this.cls = fragmentType.getFragmentClass();
            if (data != null) this.mData.putAll(data);
            this.mFragmentType = fragmentType;
        }

        public static final Creator<FragmentItem> CREATOR = new Creator<FragmentItem>() {
            @Override
            public FragmentItem createFromParcel(Parcel in) {
                return new FragmentItem(in);
            }

            @Override
            public FragmentItem[] newArray(int size) {
                return new FragmentItem[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeSerializable(cls);
            parcel.writeBundle(mData);
            parcel.writeInt(mRequestCode);
            parcel.writeSerializable(mFragmentType);
            parcel.writeInt(mAnimationEnter);
            parcel.writeInt(mAnimationExit);
        }

        public BaseFragment getFragment() {
            return mFragment;
        }
    }

}
