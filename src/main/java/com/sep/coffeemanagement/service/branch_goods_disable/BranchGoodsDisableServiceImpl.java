package com.sep.coffeemanagement.service.branch_goods_disable;

import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableReq;
import com.sep.coffeemanagement.dto.branch_goods_disable.BranchGoodsDisableRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.branch_goods_disable.BranchGoodsDisable;
import com.sep.coffeemanagement.repository.branch_goods_disable.BranchGoodsDisableRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.goods.GoodsRepository;
import com.sep.coffeemanagement.service.AbstractService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchGoodsDisableServiceImpl
  extends AbstractService<BranchGoodsDisableRepository>
  implements BranchGoodsDisableService {
  @Autowired
  private GoodsRepository goodsRepository;

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public Optional<ListWrapperResponse<BranchGoodsDisableRes>> getListGoodsIdDisableByBranch(
    String branchId
  ) {
    Branch branch = branchRepository
      .getOneByAttribute("branchId", branchId)
      .orElseThrow(() -> new ResourceNotFoundException("branch not found"));
    HashMap<String, String> allParams = new HashMap<>();
    allParams.put("branchId", branchId);
    List<BranchGoodsDisable> list = repository
      .getListOrEntity(allParams, "asc", 0, 0, "")
      .get();
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            branchGoodsDisable ->
              new BranchGoodsDisableRes(branchGoodsDisable.getGoodsId())
          )
          .collect(Collectors.toList()),
        0,
        0,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createBranchGoodsDisable(BranchGoodsDisableReq req) {
    checkValidBranchGoodsDisableRequest(req, true);
    repository.insertAndUpdate(
      objectMapper.convertValue(req, BranchGoodsDisable.class),
      false
    );
  }

  @Override
  public void removeBranchGoodsDisable(BranchGoodsDisableReq req) {
    List<BranchGoodsDisable> list = checkValidBranchGoodsDisableRequest(req, false);
    BranchGoodsDisable target = list.get(0);
    repository.removeBranchGoodsDisable(target.getId());
  }

  private List<BranchGoodsDisable> checkValidBranchGoodsDisableRequest(
    BranchGoodsDisableReq req,
    boolean isCreate
  ) {
    Map<String, String> errors = new HashMap<>();
    errors.put("goodsId", "");
    setUpBranch(req);
    validate(req);
    Goods goods = goodsRepository
      .getOneByAttribute("goodsId", req.getGoodsId())
      .orElseThrow(() -> new ResourceNotFoundException("goods not found"));
    Branch branch = branchRepository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(() -> new ResourceNotFoundException("branch not found"));
    HashMap<String, String> allParams = new HashMap<>();
    allParams.put("branchId", req.getBranchId());
    allParams.put("goodsId", req.getGoodsId());
    List<BranchGoodsDisable> list = repository
      .getListOrEntity(allParams, "asc", 0, 0, "")
      .get();
    if (!list.isEmpty() && isCreate) {
      errors.put("goodsId", "goods is disabled in branch already");
      throw new InvalidRequestException(errors, "goods is disabled in branch already");
    }
    if (list.isEmpty() && !isCreate) {
      errors.put("goodsId", "goods is not disabled in branch");
      throw new InvalidRequestException(errors, "goods is not disabled in branch");
    }
    return list;
  }

  private BranchGoodsDisableReq setUpBranch(BranchGoodsDisableReq req) {
    BranchRes branch = branchRepository
      .getBranchByManagerId(req.getUserId())
      .orElseThrow(() -> new ResourceNotFoundException("branch not found"));
    req.setBranchId(branch.getBranchId());
    return req;
  }
}
