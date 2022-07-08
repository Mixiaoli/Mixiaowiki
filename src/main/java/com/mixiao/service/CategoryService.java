package com.mixiao.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mixiao.domain.Category;
import com.mixiao.domain.CategoryExample;
import com.mixiao.mapper.CategoryMapper;
import com.mixiao.req.CategoryQueryReq;
import com.mixiao.req.CategorySaveReq;
import com.mixiao.resp.CategoryQueryResp;
import com.mixiao.resp.PageResp;
import com.mixiao.util.CopyUtil;
import com.mixiao.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service//需要声明服务层才会扫描到
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);//把代码模板导入进来
    @Resource //jdk自带的注入 @Autowired spring自带的
    private CategoryMapper categoryMapper;

    @Resource //jdk自带的注入 @Autowired spring自带的
    private SnowFlake snowFlake;//实例化

    public List<CategoryQueryResp> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return list;
    }

    //查询方法
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        //改返回值-实体类转换用for循环
        PageHelper.startPage(req.getPage(),req.getSize());//使用PageHelper分页插件 1页3条 改成动态方式获取 需要查多少 请求参数
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());
        //单体
        /**List<CategoryResp> respList = new ArrayList<>();
         for (Category category : categoryList){
         //CategoryResp categoryResp = new CategoryResp();
         //BeanUtils.copyProperties(category,categoryResp);
         //对象复制
         CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
         respList.add(categoryResp);
         }**/
        //列表
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp(); //返回参数
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**保存方法
     *
     */
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);//将请求参数变成实体传进来
        if (ObjectUtils.isEmpty(req.getId())){
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);//使用Mybatis,并且使用代码生成器后，就不需要去写sql语句了,都给你写好了
        }else {
            //更新
            categoryMapper.updateByPrimaryKey(category);//根据主建来更新
        }
    }
    /**删除方法
     *
     */
    //删除文章
    public void delete(Long id){
            categoryMapper.deleteByPrimaryKey(id);//根据Id
    }
}
