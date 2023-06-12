const countInput = document.getElementById("countInput");

function incrementCount() {
    if (count < maxCount) {
        count++;
        countInput.value = count;
    }
}

function decrementCount() {
    if (count > 1) {
        count--;
        countInput.value = count;
    }
}

function requestOrder() {

    const count = document.querySelector("#countInput").value;
    console.log(productId);
    console.log("count=" + count);

    if (count > maxCount || count < 0) {
        alert('잘못된 수량요청입니다. \n 다시 입력해 주세요');
        return;
    }
    const form = document.querySelector("#customForm");
    form.submit();
}

function requestBasket() {
    const check = confirm("장바구니에 넣으시겠습니까?");
    const count = document.querySelector("#countInput").value;
    if (check) {
        fetch(`/shopping/add?productId=${productId}&count=${count}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if (data.statusCode === 'FAIL') {
                    toastWarning(data.msg);
                    return;
                }
                toastNotice(data.msg);
            })
            .catch(data => {
                toastWarning("장바구니에 넣는 데에 실패하였습니다.");
            })
    }

}

function moreReview() {
    const formData = new FormData();
    formData.append("offset", offset);

    fetch(`/product/more/${productId}`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                toastWarning("리뷰를 더 불러올 수 없습니다.")
                button.classList.add("hidden");
                return;
            }
            data.forEach(review => {
                appendReviews(review);
                offset += 1;
            });
        })
        .catch(error => {
            console.error(error);
        });
}

function appendReviews(review) {
    const reviewList = document.querySelector("#reviewList");


    const listItem = document.createElement("li");
    listItem.className = "flex justify-between gap-x-6 py-5";

    const reviewDiv = document.createElement("div");
    reviewDiv.className = "flex gap-x-4 mx-2";

    const reviewImage = document.createElement("img");
    reviewImage.className = "h-12 w-12 flex-none rounded-full bg-gray-50";
    reviewImage.src = review.image;

    const reviewContentDiv = document.createElement("div");
    reviewContentDiv.className = "min-w-0 flex-auto";

    const reviewTitle = document.createElement("p");
    reviewTitle.className = "mt-1 text-xl leading-5 text-gray-500";
    reviewTitle.textContent = review.title;

    const reviewContent = document.createElement("p");
    reviewContent.className = "mt-1 text-xs leading-5 text-gray-500";
    reviewContent.textContent = review.content;

    reviewContentDiv.appendChild(reviewTitle);
    reviewContentDiv.appendChild(reviewContent);

    reviewDiv.appendChild(reviewImage);
    reviewDiv.appendChild(reviewContentDiv);

    const reviewInfoDiv = document.createElement("div");
    reviewInfoDiv.className = "flex flex-col items-end mx-2";

    const reviewUsername = document.createElement("p");
    reviewUsername.className = "text-xs font-semi bold leading-6 text-gray-900";
    reviewUsername.textContent = review.username;

    const reviewDate = document.createElement("p");
    reviewDate.className = "leading-6 text-sm text-gray-900";

    const isoDate = review.reviewDate;
    const formattedDate = `${isoDate.slice(0, 4)}-${isoDate.slice(5, 7)}-${isoDate.slice(8, 10)}`;
    reviewDate.textContent = "리뷰 날짜 " + formattedDate;


    reviewInfoDiv.appendChild(reviewUsername);
    reviewInfoDiv.appendChild(reviewDate);

    listItem.appendChild(reviewDiv);
    listItem.appendChild(reviewInfoDiv);

    reviewList.appendChild(listItem);

}
