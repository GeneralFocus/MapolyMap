package com.gaspercloud.gotozeal.ui.actionProductActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.view.CardForm;
import com.gaspercloud.gotozeal.R;
import com.gaspercloud.gotozeal.SessionManagement;
import com.gaspercloud.gotozeal.ThankYouPage;
import com.gaspercloud.gotozeal.model.CartModel;
import com.gaspercloud.gotozeal.model.Customers;
import com.gaspercloud.gotozeal.model.CustomersOLD;
import com.gaspercloud.gotozeal.model.LineItems;
import com.gaspercloud.gotozeal.model.OrderModel;
import com.gaspercloud.gotozeal.model.OrderModelOLD;
import com.gaspercloud.gotozeal.model.ShippingLines;
import com.gaspercloud.gotozeal.network.CheckInternetConnection;
import com.gaspercloud.gotozeal.restService.RetrofitClient;
import com.gaspercloud.gotozeal.restService.restInterface.GotoZealService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.gaspercloud.gotozeal.Constants.STATUS_CREATED_GOTOZEAL;
import static com.gaspercloud.gotozeal.Constants.createTransactionID;
import static com.gaspercloud.gotozeal.Constants.formatnumbers;
import static com.gaspercloud.gotozeal.Constants.nextActivity;
import static com.gaspercloud.gotozeal.HomeScreen.customersHome;

public class PaymentActivity extends AppCompatActivity {

    private Context context;
    //to get user session data
    private SessionManagement session;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    private CardForm cardForm;
    private AlertDialog.Builder alertBuilder;

    private Button btnBuy;
    private AppBarLayout toolbarwrap;
    private LinearLayout hidepaymentcontent;
    private ProgressBar progressBar;

    private Charge charge;
    private float aFloat;
    private CheckBox terms_payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        context = this;
        db = FirebaseFirestore.getInstance();


        progressBar = findViewById(R.id.progressBar);
        hidepaymentcontent = findViewById(R.id.hidepaymentcontent);
        toolbarwrap = findViewById(R.id.toolbarwrap);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.secured_payment));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        session = new SessionManagement(context);

        btnBuy = findViewById(R.id.btnBuy);
        terms_payment = findViewById(R.id.terms_payment);

        aFloat = Float.parseFloat(getIntent().getSerializableExtra("cartvalue_total").toString());
        btnBuy.setText(getString(R.string.make_payment, formatnumbers(aFloat)));
        if (aFloat > 0) {
            btnBuy.setVisibility(View.VISIBLE);
        } else {
            btnBuy.setVisibility(View.GONE);
        }


        cardForm = findViewById(R.id.card_form);
        cardForm.setFocusable(true);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                //.cardholderName(CardForm.FIELD_REQUIRED)
                //.postalCodeRequired(false)
                //.mobileNumberRequired(true)
                //.mobileNumberExplanation("SMS is required on this number")
                .actionLabel(getString(R.string.make_payment, formatnumbers(aFloat)))
                .setup(this);
        /*
        CardForm#setOnCardFormValidListener called when the form changes state from valid to invalid or invalid to valid.
        CardForm#setOnCardFormSubmitListener called when the form should be submitted.
        CardForm#setOnFormFieldFocusedListener called when a field in the form is focused.
        CardForm#setOnCardTypeChangedListener called when the CardType in the form changes.
         */
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        cardForm.setOnCardFormSubmitListener(new OnCardFormSubmitListener() {
            @Override
            public void onCardFormSubmit() {
                Make_payment(btnBuy);
            }
        });

        hideElementsDuringProcessing(false);
    }

    public void Notifications(View view) {
        Toasty.info(context, "Set Notifications here", Toasty.LENGTH_SHORT, true).show();
    /*startActivity(new Intent(IndividualProduct.this, NotificationActivity.class));
    finish();*/
    }

    public void Make_payment(View view) {
        if (cardForm.isValid()) {
            /*alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("Confirm before purchase");
            alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                    "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                    "Card CVV: " + cardForm.getCvv());
            alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, final int i) {
                    dialogInterface.dismiss();
                    */
            Card card = new Card(cardForm.getCardNumber(), Integer.parseInt(cardForm.getExpirationMonth()), Integer.parseInt(cardForm.getExpirationYear()), cardForm.getCvv());
            if (card.isValid()) {
                final String transRefID = getString(R.string.app_name) + "_" + createTransactionID();
                btnBuy.setEnabled(false);
                charge = new Charge();
                charge.setCard(card);


                //the amount(in KOBO eg 1000 kobo = 10 Naira) the customer is to pay for the product or service
                // basically add 2 extra zeros at the end of your amount to convert from kobo to naira.
                int amountToKobo = (int) (aFloat * 100);
                Timber.i("(int) (aFloat * 100) : " + amountToKobo);
                charge.setAmount(amountToKobo);
                //charge.setReference("ChargedFromAndroid_" + Calendar.getInstance().getTimeInMillis());
                charge.setReference(transRefID);
                charge.setCurrency("NGN");
                charge.setTransactionCharge(100);//covenience fee
                charge.setEmail(customersHome.getEmail());
                try {
                    charge.putCustomField("From GotoZeal App User", customersHome.getFirst_name() + " " + customersHome.getLast_name());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideElementsDuringProcessing(true);

                //Function to Charge user here
                //Charge the card
                PaystackSdk.chargeCard(PaymentActivity.this, charge, new Paystack.TransactionCallback() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        //btnBuy.setEnabled(true);
                        Timber.i("transaction success: " + transaction.getReference());
                        //if terms_payment is checked, save Authorization PayStack code for future use
                        if (terms_payment.isChecked()) {
                            //save details to customersHome
                        }
                        //processTheCart(true, transaction, customersHome, "");

                    }

                    @Override
                    public void beforeValidate(Transaction transaction) {
                        Timber.i("transaction beforeValidate: " + transaction.getReference());
                        //Toasty.error(context, "Invalid card details, complete payment form to proceed.", Toast.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onError(Throwable error, Transaction transaction) {
                        //btnBuy.setEnabled(true);
                        //hideElementsDuringProcessing(false);
                        Timber.e(error);
                        //Toasty.error(context, "Purchase could not be complete, " + error.getMessage(), Toast.LENGTH_LONG, true).show();
                        //processTheCart(false, transaction, customersHome, error.getMessage());
                    }
                });
            } else {
                Toasty.error(context, "Invalid card details", Toast.LENGTH_LONG, true).show();
            }
            /*    }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();*/
        } else {
            Toasty.warning(context, getString(R.string.payment_complete_form), Toast.LENGTH_SHORT, true).show();
        }
    }

    private void processTheCart(final boolean b, Transaction transaction, final CustomersOLD customersHome, final String message) {
        final String transRefID = transaction.getReference();
        final List<LineItems> lineItems = new ArrayList<>();//getting all linked product and quantity
        db.collection(context.getString(R.string.customers_table)).document(session.getUsername())
                .collection(context.getString(R.string.customers_cart))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CartModel> cartModel = queryDocumentSnapshots.toObjects(CartModel.class);
                        //Timber.i("T_cartModel: " + cartModel.toString());
                        for (final CartModel cartModel1 : cartModel) {
                            //fetch all Orders to submi back to wooOrder table before deletinf from CLoud
                                                    /*LineItems items = new LineItems();
                                                    for (Integer in : cartModel1.getProduct().getVariations()) {
                                                        if (in > 0) {
                                                            items.setVariation_id(in);
                                                        }
                                                    }
                                                    if (items.getVariation_id() > 0) {
                                                        lineItems.add(new LineItems(cartModel1.getProduct().getId(), items.getVariation_id(), cartModel1.getNo_of_items()));
                                                    } else {*/
                            lineItems.add(new LineItems(cartModel1.getProduct().getId(), cartModel1.getNo_of_items()));
                            //}
                            if (b) {
                                //only delete from cart if transaction was completed and paid
                                db.collection(context.getString(R.string.customers_table)).document(session.getUsername())
                                        .collection(getString(R.string.customers_cart)).document(String.valueOf(cartModel1.getProduct().getId()))
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Timber.d("Cart Deleted.");
                                                //delete from WishList Also
                                                db.collection(context.getString(R.string.customers_table)).document(session.getUsername())
                                                        .collection(getString(R.string.customers_wishlist)).document(String.valueOf(cartModel1.getProduct().getId()))
                                                        .delete();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Timber.e(e);
                                            }
                                        });
                            }
                        }
                    }
                })

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<ShippingLines> shippingLines = new ArrayList<>();
                            ShippingLines shippingLines1 = new ShippingLines();
                            shippingLines1.setMethod_id("flat_rate");
                            shippingLines1.setMethod_title("Flat Rate");
                            shippingLines1.setTotal("0");
                            shippingLines.add(shippingLines1);

                            OrderModelOLD orderModel = new OrderModelOLD();
                            orderModel.setCustomer_id(customersHome.getId());
                            orderModel.setBilling(customersHome.getBilling());
                            orderModel.setShipping(customersHome.getShipping());
                            orderModel.setPayment_method("PayStack");
                            orderModel.setPayment_method_title("PayStack Payment");
                            orderModel.setTransaction_id(transRefID);
                            orderModel.setLine_items(lineItems);
                            orderModel.setSet_paid(b);
                            orderModel.setStatus(b ? "processing" : "failed");
                            orderModel.setShipping_lines(shippingLines);
                            Timber.i("OrderModel: " + orderModel.toString());
                            GotoZealService service = RetrofitClient.getRetrofitInstance(getString(R.string.BASE_URL), getString(R.string.c_username), getString(R.string.c_password)).create(GotoZealService.class);

                            Call<OrderModelOLD> call = service.createOrder(orderModel);

                            //Execute the request asynchronously//

                            call.enqueue(new Callback<OrderModelOLD>() {
                                @Override
                                //Handle a successful response//
                                public void onResponse(Call<OrderModelOLD> call, Response<OrderModelOLD> response) {
                                    int status_progressCode = response.code();

                                    Timber.i(response.toString());
                                    if (status_progressCode != STATUS_CREATED_GOTOZEAL) {
                                        Timber.e(call.request().headers().toString());
                                        Toasty.error(context, response.message(), Toast.LENGTH_LONG, true).show();
                                    } else {
                                        if (message.isEmpty()) {
                                            Toasty.success(context, getString(R.string.purchaseThankYou), Toast.LENGTH_LONG, true).show();
                                        } else {
                                            Toasty.error(context, message, Toast.LENGTH_LONG, true).show();
                                        }
                                    }
                                    hideElementsDuringProcessing(false);
                                    nextActivity(context, ThankYouPage.class, "payment_status", String.valueOf(b), true);
                                    //nextActivity(context, OrderDetails.class, "", "", true);
                                }

                                @Override
                                //Handle execution failures//
                                public void onFailure(Call<OrderModelOLD> call, Throwable throwable) {
                                    Timber.e(throwable);
                                    Toasty.success(context, getString(R.string.network_error_message), Toast.LENGTH_LONG, true).show();
                                    hideElementsDuringProcessing(false);
                                }
                            });

                        } else {
                            Timber.e(task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Timber.e(e);
                Toasty.success(context, getString(R.string.network_error_message), Toast.LENGTH_LONG, true).show();
                hideElementsDuringProcessing(false);
            }
        });
    }


    private void hideElementsDuringProcessing(boolean hideView) {
        int chg = View.VISIBLE;
        if (hideView) {
            chg = View.GONE;
        }
        hidepaymentcontent.setVisibility(chg);
        btnBuy.setVisibility(chg);
        terms_payment.setVisibility(chg);
        toolbarwrap.setVisibility(chg);
        progressBar.setVisibility(chg == View.GONE ? View.VISIBLE : View.GONE);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
