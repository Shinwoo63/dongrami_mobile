document.addEventListener("DOMContentLoaded", function() {
    const container = document.getElementById('owl-card');
    if (!container) {
        console.error("Element with ID 'owl-card' not found");
        return;
    }
    container.style.transformStyle = 'preserve-3d';
    
    const content = document.querySelector('.services-caption');
    const basicTarotImage = document.getElementById('basic-tarot-image');
    const cardCount = 20;
    const startAngle = Math.PI / 55;
    const maxEndAngle = 3.1 * Math.PI / 3.15;
    const visibleAngleRange = maxEndAngle - startAngle;
    const delayBetweenCards = 15;
    let isCardsFanned = false;
    let radius = 1.0;
    let centerX, centerY;
    let selectedCard = null;
    const cardDiv = document.getElementById('card1');
    const tarot_info = document.getElementById('tarot_info');

    const cards = [];
    let selectedCount = 0;
    const maxScaleRatio = 1.2; // 확대 비율 설정

    function updateCenter() {
        const contentRect = content.getBoundingClientRect();
        centerX = contentRect.width / 2 - 145;
        centerY = contentRect.height + radius - 650; 
    }

    function resizeHandler() {
        const cardWidth = 40;
        const cardHeight = 60;
        const contentWidth = content.offsetWidth;
        const contentHeight = content.offsetHeight;

        radius = Math.min(contentWidth, contentHeight) * 0.35;

        updateCenter();

        if (isCardsFanned) {
            cards.forEach((cardObj, index) => {
                if (!cardObj.isMovedToDiv) {
                    const angle = (index / (cardCount - 1)) * visibleAngleRange + startAngle;
                    const x = centerX + radius * Math.cos(angle) - cardWidth / 2;
                    const y = centerY + radius * Math.sin(angle) - cardHeight / 2;
                    cardObj.element.style.width = `${cardWidth}px`;
                    cardObj.element.style.height = `${cardHeight}px`;
                    cardObj.element.style.transform = `translate(${x}px, ${y}px) rotate(${angle}rad)`;
                    cardObj.x = x;
                    cardObj.y = y;
                    cardObj.angle = angle;
                }
            });
        }
    }

    window.addEventListener('resize', resizeHandler);
    window.addEventListener('load', function() {
        updateCenter();
        resizeHandler();
    });

    function onCardClick() {
        const cardObj = cards.find(c => c.element === this);
        
        if (selectedCard && selectedCard !== cardObj) {
            selectedCard.element.style.transform = `translate(${selectedCard.x}px, ${selectedCard.y}px) rotate(${selectedCard.angle + Math.PI / 2}rad) scale(1)`;
        }

        if (selectedCard === cardObj) {
            if (selectedCount < 1) {
                moveCardToSelectedArea(cardObj);
            }
        } else {
            selectedCard = cardObj;
            cardObj.element.style.transformOrigin = 'center center';
            cardObj.element.style.transform = `translate(${cardObj.x}px, ${cardObj.y}px) rotate(${cardObj.angle + Math.PI / 2}rad) scale(${maxScaleRatio})`;
        }
    }

    function moveCardToSelectedArea(cardObj) {
        cardDiv.appendChild(cardObj.element);
        cardObj.element.style.transform = '';
        cardObj.element.style.position = 'relative';
        cardObj.element.style.width = '110px';
        cardObj.element.style.height = '190px';
        cardObj.element.style.marginTop = '-10px';
        cardObj.element.style.marginLeft = '-10px';
        cardObj.isMovedToDiv = true;
        selectedCount++;

        cardObj.element.removeEventListener('click', onCardClick);

        if (selectedCount === 1) {
            const showResultButton = document.getElementById('showResultButton');
            if (showResultButton) {
                showResultButton.style.display = 'flex';
            }
        }
    }
    
    function fanCards() {
        if (isCardsFanned) return;
        isCardsFanned = true;
        const cardWidth = 70;
        const cardHeight = 100;

        updateCenter();

        let cardsCreated = 0;

        for (let i = 0; i < cardCount; i++) {
            setTimeout(() => {
                const angle = (i / (cardCount - 1)) * visibleAngleRange + startAngle;
                const x = centerX + radius * Math.cos(angle) - cardWidth / 2;
                const y = centerY + radius * Math.sin(angle) - cardHeight / 2;
                const card = document.createElement('div');
                card.className = 'card';
                card.style.width = `${cardWidth}px`;
                card.style.height = `${cardHeight}px`;
                card.style.transition = 'transform 0s';
                card.style.transform = `translate(${x}px, ${y}px) rotate(${angle + Math.PI / 2}rad)`;
                card.style.position = 'absolute';
                card.style.backgroundImage = 'url(/images/owl-card.jpg)';
                card.style.backgroundSize = 'cover';
                card.style.borderRadius = '10px';
                card.style.boxShadow = '0 4px 8px rgba(0,0,0,0.1)';
                container.appendChild(card);

                const cardObj = {
                    element: card,
                    x: x,
                    y: y,
                    angle: angle,
                    isMovedToDiv: false,
                };
                cards.push(cardObj);
                
                card.addEventListener('click', onCardClick);

                cardsCreated++;

                if (cardsCreated === cardCount) {
                    setTimeout(() => {
                        if (tarot_info) {
                            tarot_info.style.display = 'flex';
                        }
                    }, delayBetweenCards * 3);
                }
            }, i * delayBetweenCards);
        }
    }

    fanCards();
   
});
