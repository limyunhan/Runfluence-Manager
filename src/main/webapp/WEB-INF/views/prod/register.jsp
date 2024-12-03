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
    
    $(document).on('change', '#variant-combination', function () {
        const selectedCombination = $(this).val();
        const variantsOptionValueListMap = JSON.parse('${variantsOptionValueListMapJsonString}'); // 서버에서 전달된 JSON 문자열

        // 기존에 추가된 옵션 값을 초기화
        $('#variant-values-container').empty();

        let values = [];

        // 선택된 조합에 따른 변형 옵션 값 처리
        switch (selectedCombination) {
            case 'CLOTH_SIZE+THEME':
                values = variantsOptionValueListMap['CLOTH_SIZE'].concat(variantsOptionValueListMap['THEME']);
                break;
            case 'SHOE_SIZE+THEME':
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

        // 선택된 옵션 값 표시
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
    
 // 조합 생성 버튼 클릭 시 동작
    $('#generate-combinations-btn').click(function() {
      const selectedOption = $('#variant-combination').val(); // 선택된 변형 옵션 조합
      const optionParts = selectedOption.split('+');
      const selectedValues = [];

      // 선택된 각 옵션에 대해 체크된 값들 저장
      optionParts.forEach(option => {
        const checkedValues = [];
        $(`.variant-value[data-option="${option}"]:checked`).each(function() {
          checkedValues.push($(this).val());
        });
        selectedValues.push(checkedValues);
      });

      // 가능한 조합 생성 (중첩된 loop를 이용한 조합 생성)
      const combinations = generateCombinations(selectedValues);

      // 결과 표시
      let combinationsHtml = '';
      combinations.forEach(combination => {
        combinationsHtml += `<div class="mb-2">조합: ${combination.join(', ')}</div>`;
      });
      $('#combinations-container').html(combinationsHtml);
      
    });
});

// 조합 생성 함수
function generateCombinations(arrays) {
  const result = [];

  // 배열 중 하나라도 비어있으면 빈 배열을 반환
  if (arrays.length === 0) return [[]];

  const firstArray = arrays[0];
  const restArrays = arrays.slice(1);

  // 첫 번째 배열의 각 항목에 대해 나머지 배열을 재귀적으로 결합
  firstArray.forEach(item => {
    const restCombinations = generateCombinations(restArrays);
    restCombinations.forEach(restCombination => {
      result.push([item, ...restCombination]);
    });
  });

  return result;
}
</script>
</head>
<body>
  <%@ include file="/WEB-INF/views/include/admin_gnb.jsp" %>
  <div class="container mt-5">
    <!-- 페이지 제목 -->
    <div class="text-center mb-4">
      <h1 class="display-5 fw-bold">제품 등록</h1>
      <p class="text-muted">새로운 제품을 등록하고 관리하세요.</p>
    </div>

    <!-- 등록 폼 -->
    <form action="" method="post" class="p-4 border rounded shadow-sm bg-light">
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
        <textarea id="prodInfo" name="prodInfo" class="form-control summernote" required placeholder="제품에 대한 설명을 작성하세요"></textarea>
      </div>

      <!-- 상태 -->
      <div class="mb-4">
        <label for="prodStatus" class="form-label fw-semibold">상태</label>
        <select id="prodStatus" name="prodStatus" class="form-select">
          <option value="A">활성화</option>
          <option value="I">비활성화</option>
          <option value="P" selected>준비중</option>
          <option value="S">품절</option>
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
                  <input type="checkbox" class="form-check-input" id="filterOption_${filterOptionValue.filterOptionValueId}" name="filterOptions" value="${filterOptionValue.filterOptionValueId}">
                  <label class="form-check-label" for="filterOption_${filterOptionValue.filterOptionValueId}">${filterOptionValue.filterOptionValueName}</label>
                </div>
              </c:forEach>
            </fieldset>
          </c:forEach>
        </div>
      </div>
      
      <!-- 변형 옵션 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">변형 옵션(해당하는 옵션들 모두 체크)</label>
        <!-- select 박스로 변경 -->
        <select id="variant-combination" class="form-select">
          <option value="CLOTH_SIZE+THEME">CLOTH_SIZE + THEME</option>
          <option value="SHOE_SIZE+THEME">SHOE_SIZE + THEME</option>
          <option value="CLOTH_SIZE">CLOTH_SIZE</option>
          <option value="SHOE_SIZE">SHOE_SIZE</option>
          <option value="DEFAULT">DEFAULT</option>
          <option value="THEME">THEME</option>
        </select>
      </div>

      <!-- 변형 옵션 값 -->
      <div class="mb-4">
        <label class="form-label fw-semibold">변형 옵션 값</label>
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
        <button type="submit" class="btn btn-primary px-5 py-2 text-nowrap">제품 등록</button>
      </div>
    </form>
  </div>
</body>
</html>