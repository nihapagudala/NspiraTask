package cgg.gov.`in`.nspiratask.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cgg.gov.`in`.nspiratask.R
import cgg.gov.`in`.nspiratask.databinding.ActivityRepoDetailsBinding
import cgg.gov.`in`.nspiratask.model.Repo
import cgg.gov.`in`.nspiratask.utils.AppConstants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RepositoryDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityRepoDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityRepoDetailsBinding>(
            this,
            R.layout.activity_repo_details
        )

        val sharedPref =getSharedPreferences(
            AppConstants.TASK, Context.MODE_PRIVATE
        )
        val json: String? =sharedPref.getString(AppConstants.DETAILS, "")
        val gson = Gson()
        val type = object : TypeToken<Repo>() {}.type
        val data: Repo = gson.fromJson(json, type)

        val content = SpannableString(data.url)
        content.setSpan(UnderlineSpan(), 0, data.url.length, 0)
        binding.repoName.setText(data.name)
        binding.repoDescription.setText(data.description)
        binding.repoUrl.setText(content)
        binding.repoStars.setText(data.stars.toString())
        binding.repoForks.setText(data.forks.toString())

        binding.repoUrl.setOnClickListener {
//            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }

            val sharedPref = getSharedPreferences(
                AppConstants.TASK, Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPref?.edit()!!
            editor.putString(AppConstants.URL, data.url)
            editor.commit()

            val intent = Intent(this, WebviewActivity::class.java)
            startActivity(intent)
        }

        /*if (!TextUtils.isEmpty(data.owner.avatarUrl)) {
            binding.pBar.setVisibility(View.VISIBLE)
            Glide.with(this)
                .load(data.owner.avatarUrl)
                .error(R.drawable.login_user)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target:com.bumptech.glide.request.target.Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pBar.setVisibility(View.GONE)
                        binding.ivUser.setVisibility(View.VISIBLE)
                        binding.ivUser.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.login_user
                            )
                        )
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pBar.setVisibility(View.GONE)
                        binding.ivUser.setVisibility(View.VISIBLE)
                        return false
                    }
                })
                .into(binding.ivUser)

        }*/
    }

}
