package com.chenjiajuan.commom.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * create by chenjiajuan on 2018/10/4
 */
public class Navigator {
    private static final MethodNavigator CALL_NAV = new MethodNavigator();
    private static final MappingNavigator<Class> TYPE_NAV = new MappingNavigator<>();

    private Navigator() {
    }

    @SuppressWarnings("unchecked")
    static void map(String format, Object target) {
        MappingNavigator navigator = getNavigator(target);
        if (navigator == null) {
            throw new IllegalStateException(String.format("unknown map: %s->%s", format, target));
        }
        navigator.map(format, target);
    }

    private static MappingNavigator getNavigator(Object target) {
        if (target instanceof Class) {
            return TYPE_NAV;
        } else if (target instanceof MethodHolder && ((MethodHolder) target).isStatic()) {
            return CALL_NAV;
        }
        return null;
    }

    /**
     * 调用远程函数，如果找不到会抛出异常{@link MappingNotFoundException}
     *
     * @param action  {@link }标记的函数名或者别名
     * @param objects 参数列表
     * @param <T>     返回对象
     * @return
     */
    public static <T> T call(String action, Object... objects) {
        MethodHolder method = CALL_NAV.nav(Uri.parse(action), objects);
        if (method == null) {
            throw new MappingNotFoundException();
        }
        return method.call(objects);
    }

    public static class ActivityRouter {

        /**
         * 本次路由地址
         */
        public static final String EXTRA_URL = "router_url";

        private final Intent intent;
        private final Object contextHolder;

        private ActivityRouter(Object contextHolder, Intent intent) {
            this.intent = intent;
            this.contextHolder = contextHolder;
        }

        private Context getContext() {
            if (contextHolder instanceof Fragment) {
                return ((Fragment) contextHolder).getActivity();
            }
            if (contextHolder instanceof android.app.Fragment) {
                return ((android.app.Fragment) contextHolder).getActivity();
            }
            if (contextHolder instanceof Activity) {
                return (Activity) contextHolder;
            }
            return (Context) contextHolder;
        }

        /**
         * 跳转到特定activity， 如果找不到会抛出异常{@link MappingNotFoundException}
         *
         * @param url 注册的路由地址
         */
        public void open(@NonNull String url) {
            open(Uri.parse(url));
        }


        /**
         * 跳转到特定activity， 如果找不到会抛出异常{@link MappingNotFoundException}
         *
         * @param uri 注册的路由地址
         */
        public void open(@NonNull Uri uri) {
            open(uri, -1);
        }


        /**
         * 跳转到特定activity，并获取返回结果， 如果找不到会抛出异常{@link MappingNotFoundException}
         * {@link Activity#onActivityResult(int, int, Intent)}
         * {@link Fragment#onActivityResult(int, int, Intent)}
         * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
         *
         * @param url         注册的路由地址
         * @param requestCode 请求码
         */
        public void open(@NonNull String url, int requestCode) {
            open(Uri.parse(url), requestCode);
        }


        /**
         * 跳转到特定activity，并获取返回结果， 如果找不到会抛出异常{@link MappingNotFoundException}
         * {@link Activity#onActivityResult(int, int, Intent)}
         * {@link Fragment#onActivityResult(int, int, Intent)}
         * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
         *
         * @param uri         注册的路由地址
         * @param requestCode 请求码
         */
        public void open(@NonNull Uri uri, int requestCode) {
            Class clazz = navByType(uri, Activity.class);
            if (clazz == null) {
                throw new MappingNotFoundException();
            }
            intent.setClass(getContext(), clazz);
            intent.putExtra(EXTRA_URL, uri.toString());
            Bundle bundle = parseExtras(uri);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (contextHolder instanceof Activity) {
                ((Activity) contextHolder).startActivityForResult(intent, requestCode);
            } else if (contextHolder instanceof Fragment) {
                ((Fragment) contextHolder).startActivityForResult(intent, requestCode);
            } else if (contextHolder instanceof android.app.Fragment) {
                ((android.app.Fragment) contextHolder).startActivityForResult(intent, requestCode);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Context) contextHolder).startActivity(intent);
            }
        }
    }

    /**
     * Activity MappingNavigator builder
     */
    public static class Builder {
        private final Object contextHolder;
        private final Intent intent = new Intent();

        public Builder(@NonNull Fragment contextHolder) {
            this.contextHolder = contextHolder;
        }

        public Builder(@NonNull Activity contextHolder) {
            this.contextHolder = contextHolder;
        }

        public Builder(@NonNull Context contextHolder) {
            this.contextHolder = contextHolder;
        }

        public Builder(@NonNull android.app.Fragment contextHolder) {
            this.contextHolder = contextHolder;
        }

        /**
         * 设置参数
         *
         * @param extras
         * @return
         */
        public Builder setExtras(Bundle extras) {
            intent.putExtras(extras);
            return this;
        }

        public Builder withExtra(String name, boolean value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, byte value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, char value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, short value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, int value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, long value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, float value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, double value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, String value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, CharSequence value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, Parcelable value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, Parcelable[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
            intent.putParcelableArrayListExtra(name, value);
            return this;
        }

        public Builder putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
            intent.putIntegerArrayListExtra(name, value);
            return this;
        }

        public Builder putStringArrayListExtra(String name, ArrayList<String> value) {
            intent.putStringArrayListExtra(name, value);
            return this;
        }

        public Builder putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
            intent.putCharSequenceArrayListExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, Serializable value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, boolean[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, byte[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, short[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, char[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, int[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, long[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, float[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, double[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, String[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, CharSequence[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public Builder withExtra(String name, Bundle value) {
            intent.putExtra(name, value);
            return this;
        }

        /**
         * 设置activity启动flag
         *
         * @param flags
         * @return
         */
        public Builder setFlags(int flags) {
            intent.setFlags(flags);
            return this;
        }

        public ActivityRouter build() {
            return new ActivityRouter(contextHolder, intent);
        }
    }

    /**
     * 获取uri中的参数并以字符串格式放入bundle
     *
     * @param uri 带参数的url
     * @return
     */
    public static Bundle parseExtras(@NonNull Uri uri) {
        Bundle bundle = null;
        for (String param : uri.getQueryParameterNames()) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(param, uri.getQueryParameter(param));
        }
        return bundle;
    }


    /**
     * 获取activity启动intent，之后版本将统一在Builder中处理
     *
     * @param context 上下文
     * @param uri     注册的uri
     * @return
     */
    @Deprecated
    public static Intent nav(@NonNull Context context, @NonNull String uri) {
        Class activityClass = navActivity(uri);
        if (activityClass == null) {
            return null;
        }
        Intent intent = new Intent(context, activityClass);
        intent.putExtra(ActivityRouter.EXTRA_URL, uri);
        return intent;
    }

    /**
     * 获取uri对应的对象
     *
     * @param url     注册的uri
     * @param objects 构造函数参数列表
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(@NonNull String url, Object... objects) {
        Class clazz = nav(url);
        if (clazz == null) {
            return null;
        }
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            try {
                return (T) constructor.newInstance(objects);
            } catch (Exception e) {
            }
        }
        throw new MappingNotFoundException();
    }

    /**
     * 获取uri对应的类
     *
     * @param uri 注册的uri
     * @return
     */
    public static Class nav(@NonNull String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        return nav(Uri.parse(uri));
    }

    /**
     * 获取uri对应的类
     *
     * @param uri 注册的uri
     * @return
     */
    public static Class nav(@NonNull Uri uri) {
        return TYPE_NAV.nav(uri);
    }

    /**
     * 获取uri对应的Activity类
     *
     * @param uri 注册的uri
     * @return
     */
    public static <T extends Activity> Class<T> navActivity(@NonNull String uri) {
        return navByType(uri, Activity.class);
    }

    /**
     * 获取uri对应的Fragment类
     *
     * @param uri 注册的uri
     * @return
     */
    public static <T extends android.app.Fragment> Class<T> navFragment(@NonNull String uri) {
        return navByType(uri, android.app.Fragment.class);
    }

    /**
     * 获取uri对应的SupportFragment类
     *
     * @param uri 注册的uri
     * @return
     */
    public static <T extends Fragment> Class<T> navSupportFragment(@NonNull String uri) {
        return navByType(uri, Fragment.class);
    }


    /**
     * 获取uri对应的指定类型的类
     *
     * @param uri 注册的uri
     * @return
     */
    public static <T> Class<T> navByType(@NonNull String uri, @NonNull Class<? super T> type) {
        return navByType(Uri.parse(uri), type);
    }


    /**
     * 获取uri对应的指定类型的类
     *
     * @param uri 注册的uri
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> navByType(@NonNull Uri uri, @NonNull Class<? super T> type) {
        Class clazz = nav(uri);
        if (clazz == null) {
            return null;
        }
        if (type.isAssignableFrom(clazz)) {
            return clazz;
        }
        return null;
    }

    /**
     * 判断路由能否路由到目标地址上
     *
     * @param router 路由地址
     * @param target 目标地址
     * @return
     */
    public static boolean isMatch(@NonNull Uri router, @NonNull Uri target) {
        if (!TextUtils.isEmpty(target.getScheme()) && !"*".equals(target.getScheme())
                && !target.getScheme().equals(router.getScheme())) {
            return false;
        }
        if (!TextUtils.isEmpty(target.getHost()) && !"*".equals(target.getHost())
                && !target.getHost().equals(router.getHost())) {
            return false;
        }
        if (!TextUtils.isEmpty(target.getPath()) && !"*".equals(target.getPath())
                && !target.getPath().equals(router.getPath())) {
            return false;
        }
        return true;
    }
}
