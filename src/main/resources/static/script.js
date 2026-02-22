document.addEventListener("DOMContentLoaded", () => {
    // DOM Elements
    const conferenceSection = document.getElementById("conference-section");
    const standSection = document.getElementById("stand-section");
    const voteSection = document.getElementById("vote-section");
    const messageSection = document.getElementById("message-section");

    const conferenceList = document.getElementById("conference-list");
    const standList = document.getElementById("stand-list");
    const currentConferenceName = document.getElementById("current-conference-name");
    const backBtn = document.getElementById("back-btn");

    const currentStandName = document.getElementById("current-stand-name");
    const currentStandInfo = document.getElementById("current-stand-info");
    const currentStandImage = document.getElementById("current-stand-image");
    const standIdInput = document.getElementById("stand-id-input");
    const voteForm = document.getElementById("vote-form");

    const statusMessage = document.getElementById("status-message");
    const okBtn = document.getElementById("ok-btn");

    // Guest ID management
    function getGuestId() {
        let guestId = localStorage.getItem("boothvote_guest_id");
        if (!guestId) {
            guestId = 'guest-' + Math.random().toString(36).substr(2, 9) + '-' + Date.now();
            localStorage.setItem("boothvote_guest_id", guestId);
        }
        return guestId;
    }

    const guestId = getGuestId();

    function hasVotedForStand(standId) {
        const votedStands = JSON.parse(localStorage.getItem("boothvote_voted_stands") || "[]");
        return votedStands.includes(parseInt(standId));
    }

    function markStandAsVoted(standId) {
        const votedStands = JSON.parse(localStorage.getItem("boothvote_voted_stands") || "[]");
        if (!votedStands.includes(parseInt(standId))) {
            votedStands.push(parseInt(standId));
            localStorage.setItem("boothvote_voted_stands", JSON.stringify(votedStands));
        }
    }

    // State
    let currentView = "conferences"; // conferences, stands, vote, message
    let history = [];

    // --- Initialization ---

    // Check for token in URL
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (token) {
        handleTokenScan(token);
    } else {
        loadConferences();
    }

    // --- Navigation ---

    function showView(viewId) {
        conferenceSection.classList.add("hidden");
        standSection.classList.add("hidden");
        voteSection.classList.add("hidden");
        messageSection.classList.add("hidden");

        document.getElementById(viewId).classList.remove("hidden");
        currentView = viewId.replace("-section", "");

        if (currentView === "conference") {
            backBtn.classList.add("hidden");
            history = [];
        } else {
            backBtn.classList.remove("hidden");
        }
    }

    backBtn.addEventListener("click", () => {
        if (history.length > 0) {
            const lastView = history.pop();
            showView(lastView);
        } else {
            showView("conference-section");
        }
    });

    function navigateTo(viewId) {
        history.push(currentView + "-section");
        showView(viewId);
    }

    // --- API Calls ---

    async function loadConferences() {
        try {
            const response = await fetch("/api/conferences");
            if (!response.ok) throw new Error("Kunne ikke hente konferanser");
            const conferences = await response.json();
            displayConferences(conferences);
        } catch (error) {
            console.error(error);
            showStatusMessage("Noe gikk galt under henting av konferanser.", false);
        }
    }

    function displayConferences(conferences) {
        conferenceList.innerHTML = "";
        if (conferences.length === 0) {
            conferenceList.innerHTML = "<li>Ingen aktive konferanser funnet.</li>";
            return;
        }

        conferences.forEach(conf => {
            const li = document.createElement("li");
            li.innerHTML = `
                <h3>${conf.name}</h3>
                <p>Starter: ${formatDate(conf.startTime)}</p>
                <p>Avsluttes: ${formatDate(conf.voteEndTime)}</p>
            `;
            li.addEventListener("click", () => loadStands(conf.id, conf.name));
            conferenceList.appendChild(li);
        });
    }

    async function loadStands(conferenceId, conferenceName) {
        try {
            const response = await fetch(`/api/stands/conference/${conferenceId}`); 
            if (!response.ok) throw new Error("Kunne ikke hente stands");
            const stands = await response.json();
            currentConferenceName.textContent = conferenceName;
            displayStands(stands);
            navigateTo("stand-section");
        } catch (error) {
            console.error(error);
            showStatusMessage("Kunne ikke hente stands for denne konferansen.", false);
        }
    }

    function displayStands(stands) {
        standList.innerHTML = "";
        if (stands.length === 0) {
            standList.innerHTML = "<li>Ingen stands registrert for denne konferansen.</li>";
            return;
        }

        stands.forEach(stand => {
            const li = document.createElement("li");
            li.innerHTML = `
                <h3>${stand.firmaNavn}</h3>
                <p>${stand.info || ""}</p>
                <p><small>Plassering: ${stand.positionIdentifier || "Ikke angitt"}</small></p>
            `;
            li.addEventListener("click", () => showVoteForm(stand));
            standList.appendChild(li);
        });
    }

    function showVoteForm(stand) {
        if (hasVotedForStand(stand.id)) {
            showStatusMessage("Takk for din stemme! Du har allerede vurdert denne standen.", true);
            return;
        }

        currentStandName.textContent = stand.firmaNavn;
        currentStandInfo.textContent = stand.info || "";
        standIdInput.value = stand.id;
        
        if (stand.bildeUrl) {
            currentStandImage.src = stand.bildeUrl;
            currentStandImage.classList.remove("hidden");
        } else {
            currentStandImage.classList.add("hidden");
        }

        voteForm.reset();
        navigateTo("vote-section");
    }

    async function handleTokenScan(token) {
        try {
            const response = await fetch(`/api/stands/scan/${token}`);
            if (!response.ok) throw new Error("Ugyldig eller utgått QR-kode");
            const stand = await response.json();
            
            if (hasVotedForStand(stand.id)) {
                showStatusMessage("Takk for din stemme! Du har allerede vurdert denne standen.", true);
                return;
            }
            
            showVoteForm(stand);
        } catch (error) {
            console.error(error);
            showStatusMessage("Ugyldig QR-kode: " + error.message, false);
            loadConferences();
        }
    }

    // --- Form Submission ---

    voteForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        
        const standId = standIdInput.value;
        const rating = voteForm.querySelector('input[name="rating"]:checked').value;
        const feedback = document.getElementById("feedback").value;

        const voteData = {
            standId: parseInt(standId),
            rating: parseInt(rating),
            feedback: feedback,
            guestId: guestId
        };

        try {
            const response = await fetch("/api/votes", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(voteData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Kunne ikke sende stemme");
            }
            
            markStandAsVoted(standId);
            showStatusMessage("Takk for din stemme!", true);
        } catch (error) {
            console.error(error);
            showStatusMessage("Noe gikk galt under sending av stemmen: " + error.message, false);
        }
    });

    // --- Helpers ---

    function showStatusMessage(message, isSuccess) {
        statusMessage.textContent = message;
        statusMessage.style.color = isSuccess ? "var(--success-color)" : "red";
        showView("message-section");
    }

    okBtn.addEventListener("click", () => {
        showView("conference-section");
        loadConferences();
    });

    function formatDate(dateString) {
        if (!dateString) return "N/A";
        const date = new Date(dateString);
        return date.toLocaleString('no-NO', { 
            day: '2-digit', 
            month: '2-digit', 
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
});