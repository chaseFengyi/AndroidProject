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
	private ImageButton back;// ���ذ�ť
	private TextView titleText;// back�ұ���ʾactivity���ܵ�text
	private ImageButton orderCicle;// ת��
	private Button eatWhat;// ת��ת����������ʾ
	private Button home;// ��ҳ
	private Button shop;// �͵�
	private Button shopping_cart;// ���ﳵ
	private Button mine;// �ҵ�
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
		// ת��ת����������ʾtext����
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
						Animation.RELATIVE_TO_SELF, 0.5f);// ������Ϊ����
				rotateAnim.setFillAfter(true);// ���ֶ������������ʽ
				rotateAnim.setDuration(2000);// ��������ʱ��
				rotateAnim.setAnimationListener(new AnimationListener() {
					String meal;

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						orderCicle.setClickable(false);// ת��ʱ���ɱ����
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
							meal = "���";
							break;
						}
						case 1: {
							meal = "����";
							break;
						}
						case 2: {
							meal = "��Ƥ";
							break;
						}
						case 3: {
							meal = "����";
							break;
						}
						case 4: {
							meal = "����";
							break;
						}
						case 5: {
							meal = "����";
							break;
						}
						}
						eatWhat.setText(meal);// ��ʾ���
						orderCicle.setClickable(true);// ת������Ա����
						eatWhat.setClickable(true);
					}
				});
				orderCicle.startAnimation(rotateAnim);

			}
		});

	}

	// ����ת�����
	private int eatWhat(int temp) {
		int eat = 0;
		switch (temp / 60) {
		case 0: {
			// ���
			eat = 0;
			break;
		}
		case 1: {
			// ����
			eat = 1;
			break;
		}
		case 2: {
			// ��Ƥ
			eat = 2;
			break;
		}
		case 3: {
			// ����
			eat = 3;
			break;
		}
		case 4: {
			// ����
			eat = 4;
			break;
		}
		case 5: {
			// ����
			eat = 5;
			break;
		}
		}
		return eat;
	}

	private void initActionbar() {
		// �Զ��������
		getActionBar().setDisplayShowHomeEnabled(false); // ʹ���Ͻ�ͼ���Ƿ���ʾ��������false����û�г���ͼ�꣬�����͸����⣬������ʾӦ�ó���ͼ��
		getActionBar().setDisplayShowTitleEnabled(false); // ��ӦActionBar.DISPLAY_SHOW_TITLE��
		getActionBar().setDisplayShowCustomEnabled(true);// ʹ�Զ������ͨView����title����ʾ
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
		titleText.setText("Ȥζ����");
		titleText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
