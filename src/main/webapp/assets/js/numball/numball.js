window.onload = function () {
    console.log("✅ window.onload 실행됨");

    const MAX_TRIES = 6;
    let existingTries = [];

    fetch("/point/numball/tries")
        .then(res => res.json())
        .then(data => {
            existingTries = JSON.parse(data.tries || "[]");
            const isSuccess = data.isSuccess;
            const tryCount = data.tryCount || 0;
            if (!data.tries || data.tries === "null") {
                console.log("⚠️ 서버에 tries 없어서 새로 저장 시도");
                saveTryToServer(existingTries);
            }

            // 복원 처리
            existingTries.forEach((row, i) => {
                const inputs = [
                    document.getElementById(`num${i}_1`),
                    document.getElementById(`num${i}_2`),
                    document.getElementById(`num${i}_3`)
                ];
                const resultBox = document.getElementById(`resultBox${i}`).querySelector(".result-box");
                const button = document.getElementById(`checkBtn${i}`);
                button.style.display = "none";

                inputs.forEach((input, idx) => {
                    input.value = row.input[idx];
                    input.disabled = true;
                    if (row.status === "success") {
                        input.classList.add("success-style");
                    } else if (row.strike === 0 && row.ball === 0) {
                        input.classList.add("out-style");
                    }
                });

                resultBox.style.display = "flex";
                resultBox.innerHTML = '';
                renderResult(resultBox, row);
            });

            if (!isSuccess && tryCount < MAX_TRIES) {
                activateRow(tryCount);
            }
        });

    function activateRow(index) {
        const input1 = document.getElementById(`num${index}_1`);
        const input2 = document.getElementById(`num${index}_2`);
        const input3 = document.getElementById(`num${index}_3`);
        const wrapper = document.getElementById(`resultBox${index}`);
        const button = wrapper.querySelector('button');
        const inputs = [input1, input2, input3];

        inputs.forEach(input => {
            input.disabled = false;
            input.value = '';
            input.addEventListener('input', validateInputs);
            input.addEventListener('keydown', handleBackspace);
        });

        button.disabled = true;
        button.classList.remove('active');
        button.addEventListener('click', handleCheckClick);

        function validateInputs(e) {
            const input = e.target;
            input.value = input.value.replace(/\D/g, '').slice(0, 1);

            const idx = inputs.indexOf(input);
            if (input.value && idx < inputs.length - 1) {
                setTimeout(() => inputs[idx + 1].focus(), 10);
            }

            const values = inputs.map(i => i.value);
            const isUnique = new Set(values).size === 3;

            if (values.every(v => v) && isUnique) {
                button.disabled = false;
                button.classList.add('active');
            } else {
                button.disabled = true;
                button.classList.remove('active');
            }
        }

        function handleBackspace(e) {
            const idx = inputs.indexOf(e.target);
            if (e.key === 'Backspace' && e.target.value === '' && idx > 0) {
                inputs[idx - 1].focus();
            }
        }

        function handleCheckClick() {
            const inputVal = input1.value + input2.value + input3.value;
            const inputArray = [input1.value, input2.value, input3.value];

            fetch('/point/numball/check', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({ input: inputVal })
            })
                .then(res => res.json())
                .then(result => {
                    const { status } = result;
                    const resultBox = wrapper.querySelector('.result-box');
                    resultBox.style.display = 'flex';
                    resultBox.innerHTML = '';
                    button.style.display = 'none';

                    renderResult(resultBox, result);

                    if (status === 'success') {
                        inputs.forEach(input => input.classList.add("success-style"));
                    } else if (result.strike === 0 && result.ball === 0) {
                        inputs.forEach(input => input.classList.add("out-style"));
                    }

                    inputs.forEach(input => {
                        input.disabled = true;
                        input.removeEventListener('input', validateInputs);
                        input.removeEventListener('keydown', handleBackspace);
                    });
                    button.removeEventListener('click', handleCheckClick);

                    existingTries.push({
                        index: index,
                        input: inputArray,
                        ...result
                    });

                    saveTryToServer(existingTries);

                    if (status !== 'success' && index + 1 < MAX_TRIES) {
                        activateRow(index + 1);
                    }
                });
        }

        setTimeout(() => input1.focus(), 100);
    }

    function renderResult(box, result) {
        if (result.status === 'success') {
            box.appendChild(makeCircle('S', 'STRIKE'));
        } else if (result.strike === 0 && result.ball === 0) {
            box.appendChild(makeCircle('O', 'OUT'));
        } else {
            if (result.strike > 0) box.appendChild(makeCircle('S', result.strike));
            if (result.ball > 0) box.appendChild(makeCircle('B', result.ball));
        }
    }

    function makeCircle(type, text) {
        const fragment = document.createDocumentFragment();

        const circle = document.createElement('div');
        circle.className = 'circle ' + type;
        circle.textContent = type;

        const label = document.createElement('span');
        label.textContent = text;

        fragment.appendChild(circle);
        fragment.appendChild(label);

        return fragment;
    }

    function saveTryToServer(allTries) {
        console.log("🚀 saveTryToServer() 호출됨", allTries);
        fetch('/point/numball/try', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                tries: allTries
            })
        });
    }
};
