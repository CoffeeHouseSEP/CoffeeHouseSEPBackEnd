package com.sep.coffeemanagement.service.branch;

import com.sep.coffeemanagement.constant.DateTime;
import com.sep.coffeemanagement.dto.branch.BranchReq;
import com.sep.coffeemanagement.dto.branch.BranchRes;
import com.sep.coffeemanagement.dto.common.ListWrapperResponse;
import com.sep.coffeemanagement.dto.image_info.ImageInfoReq;
import com.sep.coffeemanagement.exception.InvalidRequestException;
import com.sep.coffeemanagement.exception.ResourceNotFoundException;
import com.sep.coffeemanagement.repository.branch.Branch;
import com.sep.coffeemanagement.repository.branch.BranchRepository;
import com.sep.coffeemanagement.repository.image_info.ImageInfo;
import com.sep.coffeemanagement.repository.image_info.ImageInfoRepository;
import com.sep.coffeemanagement.repository.internal_user.InternalUser;
import com.sep.coffeemanagement.repository.internal_user.UserRepository;
import com.sep.coffeemanagement.service.AbstractService;
import com.sep.coffeemanagement.utils.DateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchServiceImpl
  extends AbstractService<BranchRepository>
  implements BranchService {
  @Autowired
  private ImageInfoRepository imageInfoRepository;

  @Autowired
  private UserRepository internalUserRepository;

  @Override
  public Optional<BranchRes> getBranchByManagerId(String managerId) {
    BranchRes branch = repository
      .getBranchByManagerId(managerId)
      .orElseThrow(
        () -> new ResourceNotFoundException("Không tìm thấy chi nhánh của quản lý này")
      );
    return Optional.of(
      new BranchRes(
        branch.getBranchId(),
        branch.getName(),
        branch.getAddress(),
        branch.getPhoneNumber(),
        branch.getDescription(),
        branch.getBranchManagerId(),
        branch.getStatus(),
        DateFormat.convertDateStringFormat(
          branch.getCreatedDate(),
          DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
          DateTime.YYYY_MM_DD
        ),
        DateFormat.convertDateStringFormat(
          branch.getCancelledDate(),
          DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
          DateTime.YYYY_MM_DD
        ),
        branch.getWard(),
        branch.getDistrict(),
        branch.getProvince(),
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
                branch.getStatus(),
                DateFormat.convertDateStringFormat(
                  branch.getCreatedDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                DateFormat.convertDateStringFormat(
                  branch.getCancelledDate(),
                  DateTime.YYYY_MM_DD_HH_MM_SS_HYPHEN,
                  DateTime.YYYY_MM_DD
                ),
                branch.getWard(),
                branch.getDistrict(),
                branch.getProvince(),
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
  @Transactional
  public void createBranch(BranchReq req) throws InvalidRequestException {
    checkValidBranchRequest(req);
    Branch branch = objectMapper.convertValue(req, Branch.class);
    String newId = UUID.randomUUID().toString();
    ImageInfoReq imageReq = req.getImage();
    imageReq.setObjectId(newId);
    ImageInfo imageInfo = objectMapper.convertValue(imageReq, ImageInfo.class);
    validate(imageReq);
    branch.setBranchId(newId);
    branch.setCreatedDate(DateFormat.getCurrentTime());
    branch.setCancelledDate(null);
    branch.setAddress(req.getAddress());
    branch.setWard(req.getWard());
    branch.setDistrict(req.getDistrict());
    branch.setProvince(req.getProvince());
    branch.setStatus(1);
    repository.insertAndUpdate(branch, false);
    imageInfoRepository.insertAndUpdate(imageInfo, false);
  }

  @Override
  @Transactional
  public void updateBranch(BranchReq req) {
    Branch branch = repository
      .getOneByAttribute("branchId", req.getBranchId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh"));
    checkValidBranchRequest(req);
    ImageInfo imageInfo = imageInfoRepository
      .getOneByAttribute("objectId", req.getBranchId())
      .orElse(null);
    if (imageInfo == null) {
      imageInfo = new ImageInfo();
      imageInfo.setObjectId(req.getBranchId());
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, false);
    } else {
      imageInfo.setBase64(req.getImage().getBase64());
      imageInfo.setPrefix(req.getImage().getPrefix());
      imageInfoRepository.insertAndUpdate(imageInfo, true);
    }
    Branch branchUpdate = objectMapper.convertValue(req, Branch.class);
    branchUpdate.setCreatedDate(branch.getCreatedDate());
    branchUpdate.setCancelledDate(branch.getCancelledDate());
    if (0 == req.getStatus()) {
      branchUpdate.setCancelledDate(DateFormat.getCurrentTime());
    }
    repository.insertAndUpdate(branchUpdate, true);
  }

  public void checkValidBranchRequest(BranchReq req) {
    validate(req);
    Map<String, String> errors = generateError(BranchReq.class);
    InternalUser branchManager = internalUserRepository
      .getOneByAttribute("internalUserId", req.getBranchManagerId())
      .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quản lý"));
    if (branchManager.getStatus() != 1) {
      errors.put("branchManagerId", "Người dùng không hoạt động");
      throw new InvalidRequestException(errors, "Người dùng không hoạt động");
    }
    //check branch manager co branch chua
    Optional<BranchRes> br = repository.getBranchByManagerId(req.getBranchManagerId());
    if (br.isPresent()) {
      if (!br.get().getBranchId().equals(req.getBranchId())) {
        errors.put("branchManagerId", "Người dùng đã quản lý chi nhánh khác");
        throw new InvalidRequestException(errors, "Người dùng đã quản lý chi nhánh khác");
      }
    }
  }
}
