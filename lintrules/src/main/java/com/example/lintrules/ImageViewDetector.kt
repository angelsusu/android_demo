package com.example.lintrules

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Attr

/**
 * author: beitingsu
 * created on: 2021/9/3 10:45 上午
 */
class ImageViewDetector : Detector(), Detector.XmlScanner {
    companion object {
        val ISSUE: Issue = Issue.create(
            "ImageViewNotUsedSrcCompat",
            "You must use app:srcCompat",
            "You must use app:srcCompat to avoid svg crash",
            Category.MESSAGES,
            9,
            Severity.ERROR,
            Implementation(ImageViewDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )
    }


    override fun getApplicableElements(): MutableCollection<String> {
        return mutableListOf("ImageView")
    }

    override fun getApplicableAttributes(): MutableCollection<String> {
        return mutableListOf("src")
    }

    override fun visitAttribute(context: XmlContext?, attribute: Attr?) {
        context?.report(ISSUE, context.getLocation(attribute),
            "plz use \"app:srcCompat\" instead of \"src\"")
    }

}
