package com.shopee.foody.driver.db.base

/**
 * @author zhuangchizhong
 * @date 2020/8/4
 * mark db entity class fields, to show which version the property was added.
 *
 * 目前还没有实现注解处理器来分析该值，仅用于让数据实体的字段的引入版本，在代码上更加清晰，让大家在合代码的时候避免冲突
 * 后续如果觉得新增数据字段的成本还可以优化，可以考虑用这个注解来自动生成SQL脚本，并把注解的生命周期改为[AnnotationRetention.BINARY]
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class ImportVersion(val version: Int = 1)