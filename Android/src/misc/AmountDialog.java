package misc;

import com.example.bluetoothpoker.R;

import dataModels.Account;
import dataModels.MoneyGenerated;
import database.DatabaseDataSource;
import database.PreferenceConstants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import application.PokerApplication;

public class AmountDialog extends Dialog {

	private SeekBar seekBar;
	private ImageButton dialogCancelButton;
	private ImageButton dialogOkayButton;
	private TextView label;
	
	private PokerApplication application;
	private DatabaseDataSource dbInterface;
	private SharedPreferences preferences;
	private BalanceUpdatable viewUpdatable;
	
	public AmountDialog(String message, Activity activity, BalanceUpdatable viewUpdatable)
	{
		super(activity);
		
		this.viewUpdatable = viewUpdatable;
		this.setContentView(R.layout.funds_dialog);
		this.setTitle(message);
		
		application = (PokerApplication) activity.getApplication();
		
		dialogCancelButton = (ImageButton)this.findViewById(R.id.fundsDialogCancelButton);
		dialogOkayButton = (ImageButton)this.findViewById(R.id.fundsDialogOKButton);
		label = (TextView) this.findViewById(R.id.dialogAmountText);
		
		seekBar = (SeekBar)this.findViewById(R.id.amountSeekBar);
		Account account = this.application.getAccount();
		
		label.setText(Integer.toString(account.getBalance()));
		int max = Math.max(0, this.application.MAX_GEN_BALANCE - account.getBalance());
		seekBar.setMax(max);
		seekBar.setProgress(0);
		seekBar.setOnSeekBarChangeListener(new SeekBarWatcher(R.id.amountSeekBar,this));
		
		dbInterface = application.getDataSource();
		preferences = activity.getPreferences(Context.MODE_PRIVATE);
		
		setupListeners();
	}
	
	private void setupListeners()
	{ 
		dialogCancelButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	 AmountDialog.this.dismiss();
             }
         });
		
		dialogOkayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Account account = AmountDialog.this.application.getAccount();
            	int balance = seekBar.getProgress();
            	
            	/*Extra Saftey Check*/
            	if(account.getBalance() + balance > AmountDialog.this.application.MAX_GEN_BALANCE) return;
            	
            	account.addBalance(balance);
            	
            	//@TODO - Maybe pull this into a utility class
            	if(account.isOnline())
            	{
            	MoneyGenerated generated = new MoneyGenerated(balance, account);
            	dbInterface.addMoneyGenerated(generated);
            	dbInterface.updateAccount(account);
            	}
            	else
            	{
            		Editor editor = preferences.edit();
            		editor.putInt(PreferenceConstants.OFFLINE_BALANCE, account.getBalance());
            		editor.commit();
            	}
            	
            	AmountDialog.this.viewUpdatable.updateBalance();
            	AmountDialog.this.dismiss();
            }
        });
					
					
	}
	
	public class SeekBarWatcher implements OnSeekBarChangeListener {
		
		private int id;
		private Dialog d;
		
		//Constructor
		public SeekBarWatcher(int id, Dialog d)
		{
			this.id=id;
			this.d=d;
		}

		//When the seekbar is modified by the user
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			Account account = AmountDialog.this.application.getAccount();
			
			int amount = arg1 + account.getBalance();
			TextView label = AmountDialog.this.label;
			label.setText(Integer.toString(amount));
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {}

	}
	
}
