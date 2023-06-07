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

function moreReview() {
    console.log("click!!!")
    console.log(csrfToken)
    fetch(`/more/${productId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            header: csrfToken
        },
        body: JSON.stringify({offset: offset})
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const reviews = data.reviews;
            reviews.forEach(review => {
                appendReviews(review);
                offset += 1;
            });
        })
        .catch(error => {
            console.error(error);
        });
}

function appendReviews(reviews) {
    const reviewList = document.getElementById("reviewList");

    reviews.forEach(review => {
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
        reviewDate.textContent = "리뷰 날짜 " + review.reviewDate;

        reviewInfoDiv.appendChild(reviewUsername);
        reviewInfoDiv.appendChild(reviewDate);

        listItem.appendChild(reviewDiv);
        listItem.appendChild(reviewInfoDiv);

        reviewList.appendChild(listItem);
    });
}
