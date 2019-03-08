package power.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import power.api.controller.paramModel.SearchOverLimitEventParam;
import power.api.model.OverLimitEvent;
import power.api.repository.OverLimitEventRepository;
import power.api.service.IOverLimitEventService;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 浩发 on 2019/3/5 17:37
 */
@Service
public class OverLimitEventService implements IOverLimitEventService {

    @Autowired
    private OverLimitEventRepository overLimitEventRepository;

    @Override
    public JSONObject search(SearchOverLimitEventParam params, Integer pageNo, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC,"beginDate");
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,sort);
        Page<OverLimitEvent> page = searchEvent(params,pageable);
        JSONObject result = new JSONObject();
        result.put("content",page.getContent());
        result.put("totalElements",page.getTotalElements());
        List<Integer> ids = page.getContent().stream().map(p -> p.getId()).collect(Collectors.toList());
        if (ids.size()>0) overLimitEventRepository.updateHasRead(ids);
        return result;
    }

    // jpa动态查询
    private Page<OverLimitEvent> searchEvent(SearchOverLimitEventParam param, Pageable pageable) {
        Specification<OverLimitEvent> specification = (Specification<OverLimitEvent>) (root, query, criteriaBuilder) -> {
            // 条件集合
            List<Predicate> list = new ArrayList<>();
            // 添加电表条件
            if (StringUtils.isNotEmpty(param.getMeter())) {
               list.add(criteriaBuilder.equal(root.get("meter"),param.getMeter()));
            }
            // 添加越限类型条件
            if (StringUtils.isNotEmpty(param.getType())) {
                list.add(criteriaBuilder.equal(root.get("meter"),param.getMeter()));
            }
            list.add(criteriaBuilder.greaterThan(root.get("beginDate").as(Date.class),new Date(param.getBeginDate())));
            list.add(criteriaBuilder.lessThan(root.get("endDate").as(Date.class),new Date(param.getEndDate())));
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return overLimitEventRepository.findAll(specification, pageable);
    }
}
