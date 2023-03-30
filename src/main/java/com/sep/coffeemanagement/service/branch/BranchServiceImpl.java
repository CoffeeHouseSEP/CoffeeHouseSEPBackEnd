package com.sep.coffeemanagement.service.branch;

import com.sep.coffeemanagement.dto.branch.BranchReq;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.goods.Goods;
import com.sep.coffeemanagement.repository.image_info.ImageInfo;
import com.sep.coffeemanagement.repository.image_info.ImageInfoRepository;
import com.sep.coffeemanagement.repository.news.News;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl
  extends AbstractService<BranchRepository>
  implements BranchService {
  @Autowired
  private ImageInfoRepository imageInfoRepository;

  @Override
  public Optional<BranchRes> getBranchByManagerId(String managerId) {
    return repository
      .getBranchByManagerId(managerId)
      .map(
        branch ->
          new BranchRes(
            branch.getBranchId(),
            branch.getName(),
            branch.getAddress(),
            branch.getPhoneNumber(),
            branch.getDescription(),
            branch.getBranchManagerId(),
            branch.getLongitude(),
            branch.getLatitude(),
            branch.getStatus(),
            branch.getCreatedDate(),
            branch.getCancelledDate(),
            branch.getBranchManagerName()
          )
      );
  }

  @Override
  public Optional<ListWrapperResponse<BranchRes>> getListBranch(
    Map<String, String> allParams,
    String keySort,
    int page,
    int pageSize,
    String sortField
  ) {
    List<BranchRes> list = repository.getListBranch(
      allParams,
      keySort,
      page,
      pageSize,
      sortField
    );
    return Optional.of(
      new ListWrapperResponse<>(
        list
          .stream()
          .map(
            branch ->
              new BranchRes(
                branch.getBranchId(),
                branch.getName(),
                branch.getAddress(),
                branch.getPhoneNumber(),
                branch.getDescription(),
                branch.getBranchManagerId(),
                branch.getLongitude(),
                branch.getLatitude(),
                branch.getStatus(),
                branch.getCreatedDate(),
                branch.getCancelledDate(),
                branch.getBranchManagerName()
              )
          )
          .collect(Collectors.toList()),
        page,
        pageSize,
        repository.getTotal(allParams)
      )
    );
  }

  @Override
  public void createBranch(BranchReq req) throws InvalidRequestException {
    validate(req);
    //check branch manager co branch chua
    if (repository.getBranchByManagerId(req.getBranchManagerId()).isPresent()) {
      throw new InvalidRequestException(new HashMap<>(), "user has branch already");
    }
    Branch branch = objectMapper.convertValue(req, Branch.class);

    ImageInfoReq imageReq = req.getImage();
    imageReq.setObjectId(branch.getBranchManagerId());
    ImageInfo imageInfo = objectMapper.convertValue(imageReq, ImageInfo.class);
    validate(imageReq);

    String newId = UUID.randomUUID().toString();
    branch.setBranchId(newId);
    branch.setCreatedDate(DateFormat.getCurrentTime());
    branch.setCancelledDate(null);
    branch.setStatus(1);
    repository.insertAndUpdate(branch, false);
    imageInfoRepository.insertAndUpdate(imageInfo, false);
  }

  @Override
  public void updateBranch(BranchReq req) {
    Branch branch = repository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(() -> new ResourceNotFoundException("not found"));

    HashMap<String, String> params = new HashMap<>();
    params.put("branchManagerId", req.getBranchManagerId());
    if (repository.getListBranch(params, "asc", 1, 10, "").size() > 1) {
      throw new InvalidRequestException(new HashMap<>(), "user has branch already");
    }
    ImageInfo imageInfo = objectMapper.convertValue(req.getImage(), ImageInfo.class);
    imageInfoRepository.insertAndUpdate(imageInfo, true);
    Branch branchUpdate = objectMapper.convertValue(req, Branch.class);
    validate(branchUpdate);
    repository.insertAndUpdate(branchUpdate, true);
  }
}
