<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.20/lang/summernote-ko-KR.min.js"></script>
<script>
$(document).ready(function() {
    $("#prodInfo").summernote({
        lang: 'ko-KR',
        toolbar: [
            ["insert", ['picture']],
            ["fontname", ['fontname']],
            ["fontsize", ['fontsize']],
            ["color", ['color']],
            ["style", ['style']],
            ["font", ['strikethrough', 'superscript', 'subscript']],
            ["table", ['table']],
            ["para", ['ul', 'ol', 'paragraph']],
            ["height", ['height']]
        ],
        fontNames: ['제주고딕', 'Nanum Gothic', 'Noto Sans KR', 'Spoqa Han Sans'],
        fontNamesIgnoreCheck: ['제주고딕', 'Nanum Gothic', 'Noto Sans KR', 'Spoqa Han Sans'],
        callbacks: {
        	onInit: function() {
        		$("#prodInfo").summernote("fontSize", "17");
        		$("#prodInfo").summernote("fontName", "제주고딕");
        	},
            onImageUpload: function(files) {
                for (let i = 0; i < files.length; i++) {
                    uploadImage(files[i]);
                }
            }
        }
    });
    
    $('#prodPrice').on('input', function() {
        var value = $(this).val();
        
        if (value < 0) {
            value = 0;
        }

        value = parseFloat(value).toFixed(0); 
        $(this).val(value);
    });
    
    $('#prodDiscountPercent').on('input', function() {
        var value = $(this).val();
        value = value.toString();  
        var numericValue = parseFloat(value);
        if (numericValue < 0) {
            value = '0.00';  
        } else if (numericValue > 100) {
            value = '100.00'; 
        }

        if (value.includes('.')) {
            var parts = value.split('.');
            if (parts[1].length > 2) {
                value = parseFloat(value).toFixed(2); 
            }
        } else {
            value = value + '.00';
        }

        $(this).val(value);
    });
    
    $("#reg-btn").on("click", function(e) {
    	e.preventDefault();
    	
        let selectedFilterOptions = [];
    	$('input[name="filterOptions"]:checked').each(function() {
    		 selectedFilterOptions.push($(this).val()); 
    	});

  	    $('#filterOptions').val(selectedFilterOptions.join(','));
  	    
  	    let combinations = [];	 
  	    let combinationsStock = [];
  	    let combinationsText = [];
  	    
    	$('.combination-stock').each(function () {
    	    const combination = $(this).data('combination'); 
    	    const text = $(this).data('combination_text');
            const stock = $(this).val(); 
            
            combinations.push(combination);
            combinationsStock.push(stock);
            combinationsText.push(text);
        });
    	
    	$('#combinations').val(combinations.join(','));
    	$('#combinationsStock').val(combinationsStock.join(','));
    	$('#combinationsText').val(combinationsText.join(','));
    	
    	
    	var form = $("#reg-form")[0];
    	var formData = new FormData(form);

    	$.ajax({
            type: "POST",
            url: "/prod/registerProc",
            enctype: "multipart/form-data",
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            beforeSend: function(xhr) {
                xhr.setRequestHeader("AJAX", "true");
            },
            success: function(response) {
                if (response.code === 200) {
                    alert("제품이 등록되었습니다.");
                    location.href = "/prod/register";
                    
                } else {
                	alert("서버 응답 오류로 제품 등록에 실패하였습니다.");
                }
            },
            error: function(error) {
                alert("서버 응답 오류로 제품 등록에 실패하였습니다.");
                icia.common.error(error);
            }
        });
    });
       
    $(document).on('change', '#variant-combination', function () {
        const selectedCombination = $(this).val();
        const variantsOptionValueListMap = JSON.parse('${variantsOptionValueListMapJsonString}'); 
        $('#variant-values-container').empty();

        let values = [];

        switch (selectedCombination) {
            case 'CLOTH_SIZE + THEME':
                values = variantsOptionValueListMap['CLOTH_SIZE'].concat(variantsOptionValueListMap['THEME']);
                break;
            case 'SHOE_SIZE + THEME':
                values = variantsOptionValueListMap['SHOE_SIZE'].concat(variantsOptionValueListMap['THEME']);
                break;
            case 'CLOTH_SIZE':
                values = variantsOptionValueListMap['CLOTH_SIZE'];
                break;
            case 'SHOE_SIZE':
                values = variantsOptionValueListMap['SHOE_SIZE'];
                break;
            case 'DEFAULT':
                values = variantsOptionValueListMap['DEFAULT'];
                break;
            case 'THEME':
                values = variantsOptionValueListMap['THEME'];
                break;
        }

        let html = '<fieldset class="border p-3 rounded mb-3">';
        html += '<legend class="w-auto px-2 text-primary fs-6">선택된 옵션</legend>';
        html += '<div class="row row-cols-auto g-3">';

        values.forEach(function(value) {
            html += '<div class="col">';
            html += '<div class="form-check">' +
                       '<input type="checkbox" class="form-check-input variant-value" id="value_' + value.variantsOptionValueId + '" value="' + value.variantsOptionValueId + '">' +
                       '<label class="form-check-label" for="value_' + value.variantsOptionValueId + '">' + value.variantsOptionValueName + '</label>' +
                     '</div>';
            html += '</div>';
        });

        html += '</div>';
        html += '</fieldset>';
        $('#variant-values-container').append(html);
    });
    
    $('#generate-combinations-btn').click(function () {
        const selectedOption = $('#variant-combination').val(); 
        const optionParts = selectedOption.split(' + ');
        const selectedValues = [];
        let hasEmptyOption = false;
        
        optionParts.forEach(function (option) {
            const checkedValues = [];
            $('.variant-value:checked').each(function () {
                const value = $(this).val();
                const valueParts = value.split('_'); 
                const optionGroup = valueParts.length > 2 ? valueParts[0] + '_' + valueParts[1] : valueParts[0];
                if (optionGroup === option) {
                    checkedValues.push(value);
                }
            });

            if (checkedValues.length === 0) hasEmptyOption = true;
            selectedValues.push(checkedValues);
        });
        
        if (hasEmptyOption) {
          alert('모든 옵션에서 최소한 하나 이상의 값을 선택하세요.');
          return;
        }
      
        const combinations = generateCombinations(selectedValues);
        const variantsOptionValueMap = JSON.parse('${variantsOptionValueMapJsonString}'); 
        
        
        let combinationsHtml = '';
        combinations.forEach(function(combination, index) {
        	
            const combinationNames = combination.map(function(optionId) {
                return variantsOptionValueMap[optionId]?.variantsOptionValueName || optionId; 
            });
            
            const combination_text = combinationNames.join(' / ')
        	
            combinationsHtml +=
                '<div class="mb-3 p-2 border rounded">' +
                '<strong>조합 ' + (index + 1) + ':</strong> ' + combination_text +
                '<input type="number" class="form-control mt-2 combination-stock" placeholder="재고 입력" data-combination="' + combination.join('_') + '" data-combination_text="' + combination_text + '">' +
                '</div>';
        });

        $('#combinations-container').html(combinationsHtml);
    });
});

function generateCombinations(arrays) {
  const result = [];

  if (arrays.length === 0) return [[]];

  const firstArray = arrays[0];
  const restArrays = arrays.slice(1);

  firstArray.forEach(item => {
    const restCombinations = generateCombinations(restArrays);
    restCombinations.forEach(restCombination => {
      result.push([item, ...restCombination]);
    });
  });

  return result;
}

function uploadImage(file) {
    const formData = new FormData();
    formData.append("file", file);
    
    var prodMainCateMap = {};
    <c:forEach var="entry" items="${prodMainCateMap}">
        prodMainCateMap["${entry.key}"] = {
            "prodMainCateId": "${entry.value.prodMainCateId}", 
            "prodMainCateName": "${entry.value.prodMainCateName}"
        };
    </c:forEach>
    console.log(prodMainCateMap);

    var prodSubCateMap = {};
    <c:forEach var="entry" items="${prodSubCateMap}">
        prodSubCateMap["${entry.key}"] = {
            "prodSubCateId": "${entry.value.prodSubCateId}", 
            "prodSubCateName": "${entry.value.prodSubCateName}"
        };
    </c:forEach>
    console.log(prodSubCateMap);
    
    const prodSubCateCombinedId = $("#prodSubCateCombinedId").val();
    const prodMainCateId = prodSubCateMap[prodSubCateCombinedId]?.prodMainCateId; 
    const prodMainCateName = prodMainCateMap[prodMainCateId]?.prodMainCateName || "Unknown Category";
    formData.append("prodMainCateName", prodMainCateName);
     
    $.ajax({
        type: "POST",
        url: "/prod/uploadImage",
        enctype: "multipart/form-data",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        beforeSend: function(xhr) {
            xhr.setRequestHeader("AJAX", "true");
        },
        success: function(response) {
            const imageUrl = response.url;
            const altText = response.orgName;
            
            const imgTag = document.createElement("img");
            imgTag.src = imageUrl;
            imgTag.alt = altText;
            
            $("#prodInfo").summernote("insertNode", imgTag);
        },
        error: function(error) {
            alert("이미지 업로드에 실패하였습니다.");
            icia.common.error(error);
        }
    });
}

</script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/admin_gnb.jsp"%>
  <div class="container mt-5">
    <!-- 페이지 제목 -->
    <div class="text-center mb-4">
      <h1 class="display-5 fw-bold">제품 등록</h1>
    </div>

    <form id="reg-form" name="reg-form" method="post" class="p-4 border rounded shadow-sm bg-light" enctype="multipart/form-data">
      <!-- 카테고리 선택 -->
      <div class="mb-4">
        <label for="prodSubCateCombinedId" class="form-label fw-semibold">카테고리 선택</label> 
        <select id="prodSubCateCombinedId" name="prodSubCateCombinedId" class="form-select">
          <option value="">제품 카테고리 선택</option>
          <c:forEach var="prodMainCate" items="${prodMainCateList}" varStatus="status">
            <optgroup label="${prodMainCate.prodMainCateName}">
              <c:forEach var="prodSubCate" items="${prodSubCateListMap[prodMainCate.prodMainCateId]}" varStatus="status">
                <option value="${prodSubCate.prodSubCateCombinedId}">${prodSubCate.prodSubCateName}</option>
              </c:forEach>
            </optgroup>
          </c:forEach>
        </select>
      </div>

      <!-- 제품 이름 -->
      <div class="mb-4">
        <label for="prodName" class="form-label fw-semibold">제품 이름</label> 
        <input type="text" id="prodName" name="prodName" class="form-control" maxlength="100" required placeholder="제품 이름을 입력하세요" />
      </div>

      <!-- 제품 가격 -->
      <div class="mb-4">
        <label for="prodPrice" class="form-label fw-semibold">제품 가격 (숫자만 입력)</label> 
        <input type="number" id="prodPrice" name="prodPrice" class="form-control" min="0" required placeholder="예: 10000" />
      </div>

      <!-- 제품 할인율 -->
      <div class="mb-4">
        <label for="prodDiscountPercent" class="form-label fw-semibold">제품 할인율 (%)</label> 
        <input type="number" id="prodDiscountPercent" name="prodDiscountPercent" class="form-control" step="0.01" min="0" max="100" placeholder="예: 15.5" />
      </div>

      <!-- 제품 정보 -->
      <div class="mb-4">
        <label for="prodInfo" class="form-label fw-semibold">제품 정보(폰트 : 제주고딕, 폰트 크기 : 17)</label>
        <p class="text-muted">사진 업로드 실수해도 사진 업로드되므로 주의</p>
        <p class="text-muted">반드시 카테고리 선택후에 제품 정보 작성 시작</p>
        <textarea id="prodInfo" name="prodInfo" class="form-control summernote" required placeholder="제품에 대한 설명을 작성하세요"></textarea>
      </div>
      
      <!-- 제품 이미지 업로드 -->
      <div class="mb-4">
        <label for="prodImage" class="form-label fw-semibold">제품 이미지 다중 업로드(반드시 썸네일을 가장 먼저 선택, Shift 누르면서 이미지 선택)</label>
        <p class="text-muted">
          예를 들어 첫번째 : 이미지 OU0100000001.png(썸네일)<br>
          두 번째 이미지 OU0100000001_1.png<br>
          세 번째 이미지 OU0100000001_2.png...
        </p>
        <input type="file" id="prodImage" name="prodImage" class="form-control" accept="image/*" multiple/>
      </div>

      <!-- 상태 -->
      <div class="mb-4">
        <label for="prodStatus" class="form-label fw-semibold">상태</label>
        <select id="prodStatus" name="prodStatus" class="form-select">
          <option value="A" selected>활성화</option>
          <option value="I">비활성화</option>
          <option value="P">준비중</option>
        </select>
      </div>

      <!-- 필터 옵션 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">필터 옵션(해당하는 옵션들 모두 체크)</label>
        <div>
          <c:forEach var="filterOption" items="${filterOptionList}" varStatus="status">
            <fieldset class="border p-3 rounded mb-3">
              <legend class="w-auto px-2 text-primary fs-6">${filterOption.filterOptionName}</legend>
              <c:forEach var="filterOptionValue" items="${filterOptionValueListMap[filterOption.filterOptionId]}" varStatus="status">
                <div class="form-check">
                  <input type="checkbox" class="form-check-input" id="_${filterOptionValue.filterOptionValueId}" name="filterOptions" value="${filterOptionValue.filterOptionValueId}">
                  <label class="form-check-label" for="_${filterOptionValue.filterOptionValueId}">${filterOptionValue.filterOptionValueName}</label>
                </div>
              </c:forEach>
            </fieldset>
          </c:forEach>
        </div>
      </div>

      <!-- 변형 옵션 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">변형 옵션(해당하는 옵션들 모두 체크)</label>
        <select id="variant-combination" class="form-select">
          <option value="">옵션을 선택하세요. (없을 시 DEFAULT에 체크)</option>
          <option value="CLOTH_SIZE + THEME">CLOTH_SIZE + THEME</option>
          <option value="SHOE_SIZE + THEME">SHOE_SIZE + THEME</option>
          <option value="CLOTH_SIZE">CLOTH_SIZE</option>
          <option value="SHOE_SIZE">SHOE_SIZE</option>
          <option value="DEFAULT">DEFAULT</option>
          <option value="THEME">THEME</option>
        </select>
      </div>

      <!-- 변형 옵션 값 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">변형 옵션 값(재고 수량 입력을 위해 조합을 반드시 생성할 것)</label>
        <div id="variant-values-container">
          <!-- 선택된 옵션의 값들이 동적으로 추가됨 -->
        </div>
      </div>

      <!-- 조합 생성 버튼 -->
      <div class="mb-4">
        <button type="button" class="btn btn-primary" id="generate-combinations-btn">조합 생성</button>
      </div>

      <!-- 결과 영역 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">조합 결과</label>
        <div id="combinations-container">
          <!-- 조합 결과가 여기에 동적으로 추가됨 -->
        </div>
      </div>

      <!-- 제출 버튼 -->
      <div class="text-center">
        <button id="reg-btn" class="btn btn-primary px-5 py-2 text-nowrap">제품 등록</button>
      </div>
      
      <input type="hidden" id="filterOptions" name="filterOptions" value=""></input>
      <input type="hidden" id="combinations" name="combinations" value=""></input>
      <input type="hidden" id="combinationsStock" name="combinationsStock" value=""></input>
      <input type="hidden" id="combinationsText" name="combinationsText" value=""></input>
    </form>
  </div>
</body>
</html>