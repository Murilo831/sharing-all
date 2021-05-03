package com.example.sharefacebook

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sharefacebook.databinding.ActivityMainBinding
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickShareFacebook()
        clickShareInstagram()
        clickShareTwitter()
        clickShareLinkedin()
        clickShareEmail()
        clickShareWhatsapp()
        clickShareTelegram()
        shareAll()


    }

    // TODO: Click principal do Facebook
    private fun clickShareFacebook(){
        val btnClick = findViewById<Button>(R.id.btn_share_facebook)
        btnClick?.setOnClickListener{
            shareFacebook()
        }
    }

    // Share Facebook
    private fun shareFacebook(){

            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("www.minhalojaMFM.com")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")

            val message = "$nomeProduto por apenas R$$precoProduto? Você só encontra" +
                    " na nossa loja virtual $nomeMembro no $linkAppMatchfood \uD83D\uDE03" + //@appmatchfood -> Link para o Play Store
                    "\nAceitamos várias formas de pagamento! \uD83D\uDCB3" +
                    "\nBaixe o app Matchfood e faça seu pedido no $linkSiteMatchfood"

            copyPaste(message)


            val hasTag = ShareHashtag.Builder().setHashtag("#soumatchfood #incentiveocomerciolocal #matchfood").build()
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.image)

            val imageWater = addWaterMark(imageBitmap) //todo: addWaterMark 1

            // Envia a imagem
            val photo = SharePhoto.Builder()
                .setCaption(message) //Termos do facebook -> Sets the user generated caption for the photo.
                // Note that the 'caption' must come from the user, as pre-filled content is forbidden by the Platform Policies (2.3)
                .setBitmap(imageWater)
                .build()
            val shareContentPhoto = SharePhotoContent.Builder()
                .addPhoto(photo)
                .setShareHashtag(hasTag)
                .build()

            // Como o link já pode compartilhar com text
            val shareContentLink = ShareLinkContent.Builder()
                    .setQuote(message)
                    .setShareHashtag(hasTag)
                    .setContentUrl(Uri.parse("android.resource://com.example.sharefacebook/" + R.drawable.image2)) //
                    .build()


            ShareDialog.show(this@MainActivity, shareContentPhoto)

    }

    // Todo: Logo
    private fun addWaterMark(src: Bitmap): Bitmap? {
        val w = src.width
        val h = src.height
        val result = Bitmap.createBitmap(w, h, src.config)
        val canvas = Canvas(result)

        val rectangle = Rect(0, 0, 500, 500) // Aumenta a imagem

        //canvas.drawBitmap(src, null, rectangle, null)
        val waterMark = BitmapFactory.decodeResource(resources, R.drawable.watermark)

        canvas.drawBitmap(waterMark, null, rectangle, null)
        return result
    }

    //TODO: click Principal de compartilhamento do Instagram
    private fun clickShareInstagram(){
        val btnShare = findViewById<Button>(R.id.btn_share_instagram)
        btnShare?.setOnClickListener {
            shareInstagram()
        }
    }

    // share Instagram
    private fun shareInstagram() {
                                         //com.example.sharefacebook/ -> No AndroidManifest
            val shareImage = Uri.parse("android.resource://com.example.sharefacebook/" + R.drawable.image2)

            val text = "Text instagram"

            copyPaste(text)

            createInstagramIntent(shareImage)

    }

    private fun createInstagramIntent(uri: Uri?){

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "API Testing")// Não funciona

        shareIntent.setPackage("com.instagram.android")
        startActivity(shareIntent) //-- Antigo
        //startActivity(Intent.createChooser(shareIntent, "Share to")) // Mais opções de compartilhamento
    }

    // todo: Copy text
    private fun copyPaste(text: String?){

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("simple text", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(applicationContext, "Copiado", Toast.LENGTH_SHORT).show()
    }

    //TODO: click Principal de compartilhamento do Twitter
    private fun clickShareTwitter(){
        val btnShare = findViewById<Button>(R.id.btn_share_twitter)
        btnShare?.setOnClickListener {
            shareTwitter()
        }
    }


    // Share Twitter
    private fun shareTwitter() {

            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("https://play.google.com/store/apps/details?id=com.matchfood.app")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")


            val text: String = "$nomeProduto "+"por apenas"+" R$$precoProduto? \n\n" +
                    "Você só encontra na nossa loja $nomeMembro "+"no APP"+" Matchfood " +
                    "\uD83D\uDE03 \n"+ //emoji
                    "$linkAppMatchfood \n\n" +
                    "Aceitamos várias formas de pagamento! \uD83D\uDCB3 \n\n" + //emoji
                    "Baixe o app Matchfood e faça seu pedido: \n$linkSiteMatchfood"

                                                                        //com.example.sharefacebook/ -> No AndroidManifest
            val shareImage = Uri.parse("android.resource://com.example.sharefacebook/" + R.drawable.image2)

            createTwitterIntent(shareImage, text)

    }

    private fun createTwitterIntent(uri: Uri?, text: String?){

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*" // Funciona no twitter
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        shareIntent.setPackage("com.twitter.android")
        startActivity(shareIntent) //-- Antigo
        //startActivity(Intent.createChooser(shareIntent, "Share to")) // Mais opções de compartilhamento
    }

    //TODO: click Principal de compartilhamento do Linkedin
    private fun clickShareLinkedin(){
        val btnShare = findViewById<Button>(R.id.btn_share_linkedin)
        btnShare?.setOnClickListener {
            shareLinkedin()
        }
    }

    // Compartilhar no linkedin

    private fun shareLinkedin() {

            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("https://play.google.com/store/apps/details?id=com.matchfood.app")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")


            val text: String = "$nomeProduto "+"por apenas"+" R$$precoProduto? \n\n" +
                    "Você só encontra na nossa loja $nomeMembro "+"no APP"+" Matchfood " +
                    "\uD83D\uDE03 \n"+ //emoji
                    "linkAppMatchfood \n\n" +
                    "Aceitamos várias formas de pagamento! \uD83D\uDCB3 \n\n" + //emoji
                    "Baixe o app Matchfood e faça seu pedido: \nlinkSiteMatchfood"

                                                                        //com.example.sharefacebook/ -> No AndroidManifest
            val shareImage = Uri.parse("android.resource://com.example.sharefacebook/" + R.drawable.image2)

            createLinkedinIntent(shareImage, text)

    }

    private fun createLinkedinIntent(uri: Uri?, text: String?){


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*" // Funciona no Linkedin
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        shareIntent.setPackage("com.linkedin.android")
        //shareIntent.setPackage("com.google.android.gm")
        startActivity(shareIntent) //-- Antigo
        //startActivity(Intent.createChooser(shareIntent, "Share to")) // Mais opções de compartilhamento
    }

    //TODO: click Principal de compartilhamento do Email
    private fun clickShareEmail(){
        val btnShare = findViewById<Button>(R.id.btn_share_email)
        btnShare?.setOnClickListener {
            shareEmail()
        }
    }

    // Share email

    private fun shareEmail() {

            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("https://play.google.com/store/apps/details?id=com.matchfood.app")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")


            val text: String = "$nomeProduto "+"por apenas"+" R$$precoProduto? \n\n" +
                    "Você só encontra na nossa loja $nomeMembro "+"no APP"+" Matchfood " +
                    "\uD83D\uDE03 \n"+ //emoji
                    "$linkAppMatchfood \n\n" +
                    "Aceitamos várias formas de pagamento! \uD83D\uDCB3 \n\n" + //emoji
                    "Baixe o app Matchfood e faça seu pedido: \n$linkSiteMatchfood"

                                                                        //com.example.sharefacebook/ -> No AndroidManifest
            //val shareImage = Uri.parse("android.resource://com.example.sharefacebook/"+R.drawable.image2)

            val path = R.drawable.image2.toString()
            val shareImage = Uri.fromFile(File(path))

            createEmailIntent(shareImage, text)

    }

    private fun createEmailIntent(uri: Uri?, text: String?){

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Teste de compartilhamento") // Assunto do email


        shareIntent.setPackage("com.google.android.gm")

        startActivity(shareIntent) //-- Antigo
        //startActivity(Intent.createChooser(shareIntent, "Share to")) // Mais opções de compartilhamento
    }

    //TODO: click Principal de compartilhamento do Whatsapp
    private fun clickShareWhatsapp(){
        val btnShare = findViewById<Button>(R.id.btn_share_whatsapp)
        btnShare?.setOnClickListener {
            shareWhatsapp()
        }
    }

    // Compartilhamento com o whatsapp

    private fun shareWhatsapp(){


            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("https://play.google.com/store/apps/details?id=com.matchfood.app")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")

            val text: String = "*$nomeProduto* "+"por apenas"+" *R$$precoProduto?* \n\n" +
                    "Você só encontra na nossa loja *$nomeMembro* "+"no APP"+" *Matchfood* " +
                    "\uD83D\uDE03 \n"+ //emoji
                    "$linkAppMatchfood \n\n" +
                    "Aceitamos várias formas de pagamento! \uD83D\uDCB3 \n\n" + //emoji
                    "Baixe o app Matchfood e faça seu pedido: \n$linkSiteMatchfood"
            if (!text.isEmpty()) {
                startShareTextWhatsapp(text)
            } else {
                Toast.makeText(applicationContext, "Texto esta vazio", Toast.LENGTH_SHORT).show()
            }

    }

    private fun startShareTextWhatsapp(text: String?) {
        val sendIntent = Intent()
        try{
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            //startActivity(sendIntent)

            sendIntent.setPackage("com.whatsapp") //intent.setType("vnd.android-dir/mms-sms");

            startActivity(sendIntent)
           // startActivity(Intent.createChooser(sendIntent, "Share to"))
        }catch (e: Exception){
            Toast.makeText(applicationContext, "WhatsApp not Installed", Toast.LENGTH_SHORT).show()
        }

    }

    //TODO: click Principal de compartilhamento do Telegram
    private fun clickShareTelegram(){
        val btnShare = findViewById<Button>(R.id.btn_share_telegram)
        btnShare?.setOnClickListener {
            shareTelegram()
        }
    }

    // Compartilhamento com o Telegram

    private fun shareTelegram(){

            val nomeProduto = binding.editNomeProduto.text.toString()
            val precoProduto = binding.editProductPrice.text.toString()
            val nomeMembro = binding.editMemberName.text.toString()
            val linkAppMatchfood = Uri.parse("https://play.google.com/store/apps/details?id=com.matchfood.app")
            val linkSiteMatchfood = Uri.parse("https://matchfood.com/baixe_agora")

            val text: String = "*$nomeProduto* "+"por apenas"+" *R$$precoProduto?* \n\n" +
                    "Você só encontra na nossa loja *$nomeMembro* "+"no APP"+" *Matchfood* " +
                    "\uD83D\uDE03 \n"+ //emoji
                    "$linkAppMatchfood \n\n" +
                    "Aceitamos várias formas de pagamento! \uD83D\uDCB3 \n\n" + //emoji
                    "Baixe o app Matchfood e faça seu pedido: \n$linkSiteMatchfood"
            if (!text.isEmpty()) {
                startShareTextTelegram(text)
            } else {
                Toast.makeText(applicationContext, "Texto esta vazio", Toast.LENGTH_SHORT).show()
            }

    }

    private fun startShareTextTelegram(text: String?) {
        val sendIntent = Intent()
        try {
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"

            sendIntent.setPackage("org.telegram.messenger")

            startActivity(sendIntent)
        }catch (e: Exception){
            Toast.makeText(applicationContext, "Telegram not Installed", Toast.LENGTH_SHORT).show()
        }

    }

    // Todo: Compartilha com todos

    private fun shareAll(){


        val bottomSheet = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

        bottomSheet.setContentView(view)


        val btnShare = findViewById<Button>(R.id.btn_share_all)
        btnShare?.setOnClickListener {

            bottomSheet.show()
        }

    }

    // TODO: onClicks

    fun onClickWhatsApp(view: View) {

        shareWhatsapp()
    }

    fun onClickFacebook(view: View) {

        shareFacebook()

    }

    fun onClickInstagram(view: View) {

        shareInstagram()

    }

    fun onClickTelegram(view: View) {

        shareTelegram()

    }

    fun onClickTwitter(view: View) {

        shareTwitter()

    }

    fun onClickLinkeIn(view: View) {

        shareLinkedin()

    }

    fun onClickEmail(view: View) {

        shareEmail()

    }


}

