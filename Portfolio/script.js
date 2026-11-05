const GITHUB = "https://github.com/MarcelAbad";
const icon = n => `https://cdn.jsdelivr.net/gh/devicons/devicon@v2.16.0/icons/${n}/${n}-original.svg`;

// ---------- Datos editables ----------
const skills = [
  { name: "Java",       img: icon("java") },
  { name: "Python",     img: icon("python") },
  { name: "PostgreSQL", img: icon("postgresql") },
  { name: "Linux",      img: icon("linux") },
  { name: "Git",        img: icon("git") },
  { name: "GitHub",     img: icon("github") },
  { name: "Odoo",       letter: "O" },
  { name: "RGPD",       letter: "§", note: "Protección de datos" }
];

const projects = [
  {
    title: "Backend App Club Deportivo",
    description: "API backend para la app móvil de un club deportivo: autenticación, gestión de datos corporativos y bases de datos seguras conforme al RGPD.",
    tech: ["Java", "PostgreSQL", "API REST"],
    repo: "",   // enlace al repositorio (si no es confidencial)
    demo: ""    // enlace a la demo en vivo
  },
  {
    title: "Nombre de tu proyecto",
    description: "Describe en una frase qué problema resuelve y qué aportaste tú.",
    tech: ["Python", "SQL"],
    repo: GITHUB + "?tab=repositories",
    demo: ""
  }
];

const roles = ["Desarrollador Multiplataforma", "Data Analyst", "Backend & Bases de datos"];

// ---------- Render ----------
document.getElementById("skill-grid").innerHTML = skills.map(s => `
  <div class="skill">
    ${s.img ? `<img loading="lazy" alt="" src="${s.img}">` : `<div class="ico">${s.letter}</div>`}
    <span>${s.name}${s.note ? `<small>${s.note}</small>` : ""}</span>
  </div>`).join("");

document.getElementById("project-list").innerHTML = projects.map(p => `
  <article class="project">
    <h3>${p.title}</h3>
    <p>${p.description}</p>
    <ul class="chips">${p.tech.map(t => `<li>${t}</li>`).join("")}</ul>
    <div class="links">
      ${p.repo ? `<a href="${p.repo}" target="_blank" rel="noopener">Código ↗</a>` : ""}
      ${p.demo ? `<a href="${p.demo}" target="_blank" rel="noopener">Ver en vivo ↗</a>` : ""}
    </div>
  </article>`).join("");

const logos = skills.filter(s => s.img).map(s => `<span><img alt="" src="${s.img}">${s.name}</span>`).join("");
document.getElementById("marquee").innerHTML = logos + logos + logos + logos;

// ---------- Efecto de escritura ----------
const typed = document.getElementById("typed");
let r = 0, c = 0, del = false;
(function type(){
  const word = roles[r];
  typed.textContent = word.slice(0, c);
  if (!del && c === word.length) { del = true; return setTimeout(type, 1500); }
  if (del && c === 0) { del = false; r = (r + 1) % roles.length; }
  c += del ? -1 : 1;
  setTimeout(type, del ? 35 : 70);
})();

// ---------- Aparición al hacer scroll + contadores ----------
const io = new IntersectionObserver(entries => entries.forEach(e => {
  if (!e.isIntersecting) return;
  e.target.classList.add("in");
  e.target.querySelectorAll("[data-count]").forEach(el => {
    const end = +el.dataset.count; let n = 0;
    const t = setInterval(() => { el.textContent = ++n; if (n >= end) clearInterval(t); }, 250);
  });
  io.unobserve(e.target);
}), { threshold: .15 });
document.querySelectorAll(".reveal").forEach(el => io.observe(el));

// ---------- Luz que sigue al cursor y brillo en tarjetas ----------
const root = document.documentElement;
addEventListener("pointermove", e => {
  root.style.setProperty("--mx", e.clientX + "px");
  root.style.setProperty("--my", e.clientY + "px");
});
document.querySelectorAll(".project").forEach(card => card.addEventListener("pointermove", e => {
  const b = card.getBoundingClientRect();
  card.style.setProperty("--px", (e.clientX - b.left) + "px");
  card.style.setProperty("--py", (e.clientY - b.top) + "px");
}));

// ---------- Tema claro/oscuro ----------
const saved = localStorage.getItem("theme");
if (saved) root.setAttribute("data-theme", saved);
document.getElementById("theme").addEventListener("click", () => {
  const next = root.getAttribute("data-theme") === "light" ? "dark" : "light";
  root.setAttribute("data-theme", next);
  localStorage.setItem("theme", next);
});

document.getElementById("year").textContent = new Date().getFullYear();