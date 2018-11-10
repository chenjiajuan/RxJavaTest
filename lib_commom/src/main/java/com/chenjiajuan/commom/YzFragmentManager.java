package com.chenjiajuan.commom;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.chenjiajuan.commom.base.BaseFragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunmeng on 2016/10/14.
 */
public class YzFragmentManager {

    /**
     * 一般模式，每次重新创建fragment
     */
    public static final int LAUNCH_MULTIPLE = 0;

    /**
     * 单例模式，如果已经显示过会销毁上面所有fragment并调用{@link BaseFragment#onNewInstance(Bundle)}
     */
    public static final int LAUNCH_POP = 1;

    /**
     * 单例模式，如果已经显示过会显示到最上层并调用{@link BaseFragment#onNewInstance(Bundle)}
     */
    public static final int LAUNCH_PULL = 2;

    /**
     * 替换模式，删除所有fragment，重新创建fragment
     */
    public static final int LAUNCH_REPLACE = 3;

    private FragmentManager fragmentManager;
    private SparseArray<List<SoftReference<BaseFragment>>> mFragmentHeap = new SparseArray<>();

    class FragmentLifeObserver implements LifecycleObserver {

        private SoftReference<BaseFragment> reference;

        private FragmentLifeObserver(BaseFragment fragment) {
            reference = new SoftReference<>(fragment);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void onCreate() {
            onFragmentCreate(reference.get());
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void onResume() {
            // onFragmentVisible(reference.get());
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy() {
            onFragmentDestroy(reference.get());
            BaseFragment fragment = reference.get();
            if (fragment != null) {
                fragment.getLifecycle().removeObserver(this);
            }
        }
    }


    public YzFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * 修改栈中顺序
     *
     * @param baseFragment 显示的fragment
     */
    private void onFragmentVisible(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(baseFragment.getId());
        if (list == null) {
            return;
        }
        for (SoftReference<BaseFragment> reference : list) {
            if (reference == null) {
                continue;
            }
            if (reference.get() == baseFragment) {
                list.remove(reference);
                list.add(reference);
                return;
            }
        }
    }

    /**
     * 入栈
     *
     * @param baseFragment
     */
    private void onFragmentCreate(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(baseFragment.getId());
        if (list == null) {
            list = new ArrayList<>();
            mFragmentHeap.put(baseFragment.getId(), list);
        }
        list.add(new SoftReference<>(baseFragment));
    }

    /**
     * 出栈，并显示栈顶fragment
     *
     * @param baseFragment
     */
    private void onFragmentDestroy(BaseFragment baseFragment) {
        if (baseFragment == null) {
            return;
        }
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(baseFragment.getId());
        if (list == null) {
            return;
        }
        for (SoftReference<BaseFragment> reference : list) {
            if (reference == null) {
                continue;
            }
            if (reference.get() == baseFragment) {
                list.remove(reference);
                break;
            }
        }
        // 显示栈顶fragment
        BaseFragment fragment = top(baseFragment.getId(), BaseFragment.class);
        if (fragment != null) {
            Fragment parentFragment = fragment.getParentFragment();
            if (parentFragment != null && parentFragment.isRemoving()) {
                return;
            }

            Activity activity = fragment.getActivity();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && activity.isDestroyed()) {
                return;
            }
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.show(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    /**
     * 获取当前layout上显示的fragment
     *
     * @param containerViewId
     * @return
     */
    @Nullable
    public <T extends BaseFragment> T findVisible(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }
        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment baseFragment;
            if (reference == null
                    || (baseFragment = reference.get()) == null) {
                continue;
            }
            return (T) baseFragment;
        }
        return null;
    }

    /**
     * finish containerViewid 上面所有的fragments
     *
     * @param containerViewId
     */
    public void finishFragments(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Fragment fragment = list.get(i).get();
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment)
                            .commit();
                }
            }
        }

        mFragmentHeap.remove(containerViewId);
    }

    /**
     * 获取存在的指定类型的fragment
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T find(@IdRes int containerViewId, @NonNull Class<T> clazz) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }
        for (SoftReference<BaseFragment> reference : list) {
            BaseFragment baseFragment;
            if (reference == null
                    || (baseFragment = reference.get()) == null
                    || clazz != baseFragment.getClass()) {
                continue;
            }
            return (T) baseFragment;
        }
        return null;
    }

    /**
     * 获取最上层的fragment
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T topVisible(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }
        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment baseFragment;
            if (reference == null
                    || (baseFragment = reference.get()) == null) {
                continue;
            }
            return (T) baseFragment;
        }
        return null;
    }

    /**
     * 获取指定类型的最上层的fragment
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends BaseFragment> T top(@IdRes int containerViewId, @NonNull Class<T> clazz) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return null;
        }
        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment baseFragment;
            if (reference == null
                    || (baseFragment = reference.get()) == null
                    || !clazz.isInstance(baseFragment)) {
                continue;
            }
            return (T) baseFragment;
        }
        return null;
    }

    /**
     * 查找fragment的index
     *
     * @param containerViewId
     * @param t
     * @param <T>
     * @return
     */
    private <T extends BaseFragment> int index(@IdRes int containerViewId, @NonNull T t) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        if (list == null) {
            return -1;
        }
        for (int index = list.size() - 1; index >= 0; index--) {
            SoftReference<BaseFragment> reference = list.get(index);
            BaseFragment baseFragment;
            if (reference == null || (baseFragment = reference.get()) == null) {
                continue;
            }
            if (baseFragment == t) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 当前containerViewId下Fragment的数量
     *
     * @param containerViewId
     * @param <T>
     * @return
     */
    public <T extends BaseFragment> int size(@IdRes int containerViewId) {
        List<SoftReference<BaseFragment>> list = mFragmentHeap.get(containerViewId);
        return list == null ? 0 : list.size();
    }

    /**
     * 展示fragment with animation
     *
     * @param context
     * @param containerViewId
     * @param clazz
     * @param bundle
     * @param flag
     * @param enter
     * @param exit
     * @param popEnter
     * @param popExit
     * @param <T>
     * @return
     */
    public <T extends BaseFragment> T showWithAnimation(Context context,
                                                        @IdRes int containerViewId,
                                                        @NonNull Class<T> clazz,
                                                        Bundle bundle,
                                                        int flag, @AnimatorRes @AnimRes int
                                                                enter, @AnimatorRes @AnimRes int
                                                                exit,
                                                        @AnimatorRes @AnimRes int popEnter,
                                                        @AnimatorRes @AnimRes int popExit) {
        return showAction(context, containerViewId, clazz, bundle, flag, enter, exit, popEnter,
                popExit);
    }

    private <T extends BaseFragment> T showAction(
            Context context,
            @IdRes int containerViewId,
            @NonNull Class<T> clazz,
            Bundle bundle,
            int flag, @AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit,
            @AnimatorRes @AnimRes int popEnter, @AnimatorRes @AnimRes int popExit) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        /*
        参考setCustomAnimations实现，是判断是否 == 0
        if (mAnimationInfo == null && animResourceId == 0) {
            return; // no change!
        }

        资源ID也是可以是负数的
        */
        final int NO_ANIM = 0;
        if (enter != NO_ANIM || exit != NO_ANIM || popEnter != NO_ANIM || popExit != NO_ANIM) {
            ft.setCustomAnimations(enter, exit, popEnter, popExit);
        }

        BaseFragment prev = findVisible(containerViewId);
        T t;
        switch (flag) {
            case LAUNCH_REPLACE:
                t = (T) Fragment.instantiate(context, clazz.getName(), bundle);
                t.getLifecycle().addObserver(new FragmentLifeObserver(t));
                ft.replace(containerViewId, t);
                break;
            case LAUNCH_PULL:
            case LAUNCH_POP:
                t = find(containerViewId, clazz);
                if (t != null) {
                    int index;
                    if (flag == LAUNCH_POP && (index = index(containerViewId, t)) != -1) {
                        for (index++; index < mFragmentHeap.get(containerViewId).size(); index++) {
                            SoftReference<BaseFragment> reference =
                                    mFragmentHeap.get(containerViewId).get(index);
                            BaseFragment fragment;
                            if (reference == null || (fragment = reference.get()) == null) {
                                continue;
                            }
                            ft.remove(fragment);
                        }
                    }
                    t.onNewInstance(bundle);
                    onFragmentVisible(t);
                    break;
                }
            case LAUNCH_MULTIPLE:
            default:
                t = (T) Fragment.instantiate(context, clazz.getName(), bundle);
                t.getLifecycle().addObserver(new FragmentLifeObserver(t));
                ft.add(containerViewId, t);
        }

        if (prev != null && prev.isAdded() && prev != t) {
            ft.hide(prev);
        }
        ft.show(t);
        ft.commitAllowingStateLoss();
        return t;
    }

    /**
     * @param context
     * @param containerViewId
     * @param clazz
     * @param bundle
     * @param flag            {@link #LAUNCH_MULTIPLE}
     *                        {@link #LAUNCH_POP}
     *                        {@link #LAUNCH_PULL}
     *                        {@link #LAUNCH_REPLACE}
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseFragment> T show(
            Context context,
            @IdRes int containerViewId,
            @NonNull Class<T> clazz,
            Bundle bundle,
            int flag) {
        return showAction(context, containerViewId, clazz, bundle, flag, 0, 0, 0, 0);
    }
}
