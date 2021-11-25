package com.example.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

/**
 * author: beitingsu
 * created on: 2021/9/3 11:21 上午
 */
class ImageViewLintRegistry : IssueRegistry() {
    override fun getIssues(): MutableList<Issue> {
        return mutableListOf(ImageViewDetector.ISSUE)
    }
}