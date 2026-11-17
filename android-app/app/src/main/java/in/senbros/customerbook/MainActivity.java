package in.senbros.customerbook;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private final int green=Color.rgb(25,61,52), gold=Color.rgb(177,139,76), ink=Color.rgb(35,45,40);
    private Store store; private LinearLayout root, list; private EditText search; private long pdfCustomerId;
    private final ActivityResultLauncherCompat pdfLauncher = new ActivityResultLauncherCompat(this);

    @Override public void onCreate(Bundle b) { super.onCreate(b); store=new Store(this); showHome(); }
    private TextView text(String value,int size,int color){ TextView t=new TextView(this); t.setText(value);t.setTextSize(size);t.setTextColor(color);t.setPadding(0,6,0,6);return t; }
    private LinearLayout column(){ LinearLayout l=new LinearLayout(this);l.setOrientation(1);l.setPadding(20,16,20,24);return l; }
    private Button button(String label){ Button b=new Button(this);b.setText(label);b.setAllCaps(false);return b; }
    private void shell(String title){
        root=column(); root.setBackgroundColor(Color.rgb(248,247,243)); ScrollView sc=new ScrollView(this);sc.addView(root);setContentView(sc);
        TextView brand=text("SEN BROS GUINEA PALACE",13,gold);brand.setLetterSpacing(.12f);root.addView(brand);
        TextView heading=text(title,27,green);heading.setTypeface(null,1);root.addView(heading);
    }
    private void showHome(){
        shell("Customer Book"); root.addView(text("Customer records and purchase history · saved on this device",14,ink));
        Button add=button("＋  Add customer");root.addView(add);add.setOnClickListener(v->customerForm());
        search=new EditText(this);search.setSingleLine();search.setHint("Search name, customer code, phone");root.addView(search);
        list=column();list.setPadding(0,8,0,0);root.addView(list);search.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int a,int c,int f){}public void onTextChanged(CharSequence s,int a,int before,int count){loadCustomers(s.toString());}public void afterTextChanged(android.text.Editable e){}});loadCustomers("");
    }
    private void loadCustomers(String q){ if(list==null)return;list.removeAllViews();Cursor c=store.getReadableDatabase().rawQuery("SELECT id,code,name,phone FROM customers WHERE name LIKE ? OR code LIKE ? OR phone LIKE ? ORDER BY name",new String[]{"%"+q+"%","%"+q+"%","%"+q+"%"});
        while(c.moveToNext()){long id=c.getLong(0);LinearLayout card=column();card.setPadding(16,10,16,10);card.setBackgroundColor(Color.WHITE);TextView name=text(c.getString(2),18,green);name.setTypeface(null,1);card.addView(name);card.addView(text(c.getString(1)+"  ·  "+c.getString(3),13,ink));Button open=button("Open record");card.addView(open);open.setOnClickListener(v->showCustomer(id));list.addView(card);View gap=new View(this);gap.setLayoutParams(new LinearLayout.LayoutParams(1,10));list.addView(gap);}c.close();if(list.getChildCount()==0)list.addView(text("No customers yet. Add your first customer to get started.",15,ink)); }
    private EditText field(String hint){ EditText e=new EditText(this);e.setSingleLine();e.setHint(hint);return e; }
    private void customerForm(){
        LinearLayout form=column(); EditText name=field("Full name *"),address=field("Address"),phone=field("Phone number *"),alt=field("Alternate phone"),email=field("Email");
        for(EditText e:new EditText[]{name,address,phone,alt,email})form.addView(e);
        new AlertDialog.Builder(this).setTitle("New customer").setView(form).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{if(name.getText().toString().trim().isEmpty()||phone.getText().toString().trim().isEmpty()){toast("Name and phone are required");return;}String code=store.addCustomer(name.getText().toString(),address.getText().toString(),phone.getText().toString(),alt.getText().toString(),email.getText().toString());toast("Saved as "+code);showHome();}).show();
    }
    private void showCustomer(long id){
        Cursor c=store.getReadableDatabase().rawQuery("SELECT code,name,address,phone,alt,email FROM customers WHERE id=?",new String[]{""+id});if(!c.moveToFirst()){c.close();return;}
        String code=c.getString(0),name=c.getString(1);shell(name);root.addView(text("Customer code: "+code,15,gold));root.addView(text("Address: "+blank(c.getString(2))+"\nPhone: "+blank(c.getString(3))+"\nAlternate phone: "+blank(c.getString(4))+"\nEmail: "+blank(c.getString(5)),15,ink));c.close();
        root.addView(text("Purchase history",20,green));Cursor p=store.getReadableDatabase().rawQuery("SELECT date,item,quantity,price,payment,paid FROM purchases WHERE customer_id=? ORDER BY date DESC,id DESC",new String[]{""+id});double total=0;
        while(p.moveToNext()){double line=p.getDouble(2)*p.getDouble(3);total+=line;root.addView(text(p.getString(0)+"  |  "+p.getString(1)+"\nQty "+p.getString(2)+" × ₹"+p.getString(3)+" = ₹"+String.format(Locale.US,"%.2f",line)+"\nPayment: "+p.getString(4)+" · Paid ₹"+p.getString(5),14,ink));}
        p.close();root.addView(text("Purchase total: ₹"+String.format(Locale.US,"%.2f",total),17,green));Button add=button("＋  Add purchase");root.addView(add);add.setOnClickListener(v->purchaseForm(id));Button pdf=button("Export customer PDF");root.addView(pdf);pdf.setOnClickListener(v->{pdfCustomerId=id;pdfLauncher.create(code+"-customer.pdf");});Button back=button("←  All customers");root.addView(back);back.setOnClickListener(v->showHome());
    }
    private String blank(String x){return x==null||x.trim().isEmpty()?"—":x;}
    private void purchaseForm(long id){LinearLayout f=column();EditText date=field("Date (YYYY-MM-DD)");date.setText(new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date()));EditText item=field("Item / description *"),qty=field("Quantity"),price=field("Unit price (₹)"),payment=field("Payment details (cash, UPI, card...)"),paid=field("Amount paid (₹)");for(EditText e:new EditText[]{date,item,qty,price,payment,paid})f.addView(e);
        new AlertDialog.Builder(this).setTitle("Add purchase").setView(f).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->{try{if(item.getText().toString().trim().isEmpty())throw new Exception();store.addPurchase(id,date.getText().toString(),item.getText().toString(),Double.parseDouble(or(qty.getText().toString(),"1")),Double.parseDouble(or(price.getText().toString(),"0")),payment.getText().toString(),Double.parseDouble(or(paid.getText().toString(),"0")));showCustomer(id);}catch(Exception ex){toast("Enter an item and valid quantity, price and amount");}}).show();}
    private String or(String x,String fallback){return x.trim().isEmpty()?fallback:x.trim();}
    private void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
    private void exportPdf(Uri uri){
        if(uri==null)return;Cursor c=store.getReadableDatabase().rawQuery("SELECT code,name,address,phone,alt,email FROM customers WHERE id=?",new String[]{""+pdfCustomerId});if(!c.moveToFirst()){c.close();return;}String[] info={"Customer code: "+c.getString(0),"Name: "+c.getString(1),"Address: "+blank(c.getString(2)),"Phone: "+blank(c.getString(3)),"Alternate phone: "+blank(c.getString(4)),"Email: "+blank(c.getString(5))};c.close();
        PdfDocument pdf=new PdfDocument();PdfDocument.Page page=pdf.startPage(new PdfDocument.PageInfo.Builder(595,842,1).create());Canvas canvas=page.getCanvas();Paint paint=new Paint(3);paint.setColor(green);paint.setTextSize(20);canvas.drawText("SEN BROS GUINEA PALACE",40,48,paint);paint.setTextSize(16);canvas.drawText("Customer & Purchase Statement",40,78,paint);paint.setColor(ink);paint.setTextSize(11);int y=110;for(String s:info){canvas.drawText(s,40,y,paint);y+=20;}y+=14;paint.setFakeBoldText(true);canvas.drawText("Date",40,y,paint);canvas.drawText("Item",112,y,paint);canvas.drawText("Qty",310,y,paint);canvas.drawText("Unit ₹",355,y,paint);canvas.drawText("Total ₹",415,y,paint);canvas.drawText("Payment / paid",475,y,paint);paint.setFakeBoldText(false);y+=10;canvas.drawLine(40,y,555,y,paint);y+=18;
        Cursor p=store.getReadableDatabase().rawQuery("SELECT date,item,quantity,price,payment,paid FROM purchases WHERE customer_id=? ORDER BY date,id",new String[]{""+pdfCustomerId});while(p.moveToNext()){if(y>790){pdf.finishPage(page);page=pdf.startPage(new PdfDocument.PageInfo.Builder(595,842,pdf.getPages().size()+1).create());canvas=page.getCanvas();y=50;}String item=p.getString(1);if(item.length()>27)item=item.substring(0,24)+"...";canvas.drawText(p.getString(0),40,y,paint);canvas.drawText(item,112,y,paint);canvas.drawText(p.getString(2),310,y,paint);canvas.drawText(String.format(Locale.US,"%.2f",p.getDouble(3)),355,y,paint);canvas.drawText(String.format(Locale.US,"%.2f",p.getDouble(2)*p.getDouble(3)),415,y,paint);canvas.drawText(p.getString(4)+" / "+p.getString(5),475,y,paint);y+=22;}p.close();pdf.finishPage(page);
        try(OutputStream out=getContentResolver().openOutputStream(uri)){pdf.writeTo(out);toast("PDF saved");}catch(Exception e){toast("Could not save PDF");}finally{pdf.close();}
    }
    private static class ActivityResultLauncherCompat {private final MainActivity a;ActivityResultLauncherCompat(MainActivity a){this.a=a;}void create(String name){Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType("application/pdf");i.putExtra(Intent.EXTRA_TITLE,name);a.startActivityForResult(i,91);}}
    @Override protected void onActivityResult(int request,int result,Intent data){super.onActivityResult(request,result,data);if(request==91&&result==RESULT_OK&&data!=null)exportPdf(data.getData());}

    private static class Store extends SQLiteOpenHelper {
        Store(Context c){super(c,"senbros_customers.db",null,1);}
        public void onCreate(SQLiteDatabase db){db.execSQL("CREATE TABLE customers(id INTEGER PRIMARY KEY AUTOINCREMENT,code TEXT UNIQUE,name TEXT NOT NULL,address TEXT,phone TEXT NOT NULL,alt TEXT,email TEXT)");db.execSQL("CREATE TABLE purchases(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id INTEGER NOT NULL,date TEXT,item TEXT NOT NULL,quantity REAL,price REAL,payment TEXT,paid REAL,FOREIGN KEY(customer_id) REFERENCES customers(id))");}
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}
        String addCustomer(String name,String address,String phone,String alt,String email){SQLiteDatabase db=getWritableDatabase();db.beginTransaction();try{ContentValues v=new ContentValues();v.put("name",name.trim());v.put("address",address);v.put("phone",phone);v.put("alt",alt);v.put("email",email);long id=db.insertOrThrow("customers",null,v);String code=String.format(Locale.US,"SBGP-%06d",id);ContentValues u=new ContentValues();u.put("code",code);db.update("customers",u,"id=?",new String[]{""+id});db.setTransactionSuccessful();return code;}finally{db.endTransaction();}}
        void addPurchase(long customer,String date,String item,double qty,double price,String payment,double paid){ContentValues v=new ContentValues();v.put("customer_id",customer);v.put("date",date);v.put("item",item);v.put("quantity",qty);v.put("price",price);v.put("payment",payment);v.put("paid",paid);getWritableDatabase().insertOrThrow("purchases",null,v);}
    }
}
