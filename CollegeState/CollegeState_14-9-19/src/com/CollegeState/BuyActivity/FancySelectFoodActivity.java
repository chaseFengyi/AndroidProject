package com.CollegeState.BuyActivity;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.CollegeState.R;

/**
 * 
 * @author F.Crazy
 * 
 */
public class FancySelectFoodActivity extends Activity {
	private ImageButton back;// 返回按钮
	private TextView titleText;// back右边显示activity功能的text
	private ImageButton orderCicle;// 转盘
	private Button eatWhat;// 转盘转动结束后显示
	private Button home;// 首页
	private Button shop;// 餐店
	private Button shopping_cart;// 购物车
	private Button mine;// 我的
	private int temp;
	private Intent intent;

	public void findView() {
		home = (Button) findViewById(R.id.fancy_home);
		shop = (Button) findViewById(R.id.fancy_candian);
		shopping_cart = (Button) findViewById(R.id.fancy_shopping_cart);
		mine = (Button) findViewById(R.id.fancy_wode);
		orderCicle = (ImageButton) findViewById(R.id.fancy_circle);
		eatWhat = (Button) findViewById(R.id.ordering_eatWhat);
		eatWhat.setClickable(false);
	}

	public void setListener() {
		// 转盘转动结束后显示text监听
		eatWhat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(FancySelectFoodActivity.this,
						ShopActivity.class);
				intent.putExtra("type", eatWhat(temp - 1080) + "");
				FancySelectFoodActivity.this.startActivity(intent);
			}
		});

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(FancySelectFoodActivity.this,
						MainActivity.class);
				FancySelectFoodActivity.this.startActivity(intent);
			}
		});

		shopping_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent();
				intent.setClass(FancySelectFoodActivity.this,
						ShoppingCartActivity.class);
				startActivity(intent);
			}
		});

		shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("type", "shop");
				intent.setClass(FancySelectFoodActivity.this,
						ShopActivity.class);
				FancySelectFoodActivity.this.startActivity(intent);
			}
		});

		mine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(FancySelectFoodActivity.this,
						MoreActivity.class);
				FancySelectFoodActivity.this.startActivity(intent);
			}
		});
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fancy_select_food);
		findView();
		setListener();
		initActionbar();

		orderCicle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RotateAnimation rotateAnim = null;
				temp = ((int) (Math.random() * 6)) * 60 + 1080;
				rotateAnim = new RotateAnimation(0, -temp,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);// 以自身为中心
				rotateAnim.setFillAfter(true);// 保持动画结束后的样式
				rotateAnim.setDuration(2000);// 动画持续时间
				rotateAnim.setAnimationListener(new AnimationListener() {
					String meal;

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						orderCicle.setClickable(false);// 转动时不可被点击
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub

						switch (eatWhat(temp - 1080)) {
						case 0: {
							meal = "快餐";
							break;
						}
						case 1: {
							meal = "炒菜";
							break;
						}
						case 2: {
							meal = "凉皮";
							break;
						}
						case 3: {
							meal = "面条";
							break;
						}
						case 4: {
							meal = "饺子";
							break;
						}
						case 5: {
							meal = "其他";
							break;
						}
						}
						eatWhat.setText(meal);// 显示结果
						orderCicle.setClickable(true);// 转动后可以被点击
						eatWhat.setClickable(true);
					}
				});
				orderCicle.startAnimation(rotateAnim);

			}
		});

	}

	// 计算转动结果
	private int eatWhat(int temp) {
		int eat = 0;
		switch (temp / 60) {
		case 0: {
			// 快餐
			eat = 0;
			break;
		}
		case 1: {
			// 炒菜
			eat = 1;
			break;
		}
		case 2: {
			// 凉皮
			eat = 2;
			break;
		}
		case 3: {
			// 面条
			eat = 3;
			break;
		}
		case 4: {
			// 饺子
			eat = 4;
			break;
		}
		case 5: {
			// 其他
			eat = 5;
			break;
		}
		}
		return eat;
	}

	private void initActionbar() {
		// 自定义标题栏
		getActionBar().setDisplayShowHomeEnabled(false); // 使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
		getActionBar().setDisplayShowTitleEnabled(false); // 对应ActionBar.DISPLAY_SHOW_TITLE。
		getActionBar().setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mTitleView = mInflater.inflate(
				R.layout.custom_action_bar_onlyback, null);
		getActionBar().setCustomView(
				mTitleView,
				new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleText = (TextView) findViewById(R.id.title_name);
		titleText.setText("趣味订餐");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
