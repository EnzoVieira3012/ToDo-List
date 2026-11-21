# =============================================================================
# Frontend - Multi-stage Docker Build
# =============================================================================
# Stage 1: Build com Node 20 + Vite
# =============================================================================
FROM node:20-alpine AS builder

# Build argument para URL da API
ARG VITE_API_BASE_URL=http://localhost:8080/api
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

WORKDIR /app

# Copiar package files primeiro para cache de dependências
COPY package*.json ./

# Instalar dependências (ci = clean install, mais rápido e determinístico)
RUN npm ci

# Copiar código fonte
COPY . .

# Build de produção (TypeScript check + Vite build)
RUN npm run build

# =============================================================================
# Stage 2: Runtime com Nginx Alpine (imagem mínima ~25MB)
# =============================================================================
FROM nginx:alpine AS runner

# Metadados
LABEL maintainer="To-Do List Team"
LABEL description="To-Do List Frontend - React 18 + TypeScript + Vite + Tailwind"
LABEL version="1.0.0"

# Remover configuração padrão do nginx
RUN rm /etc/nginx/conf.d/default.conf

# Copiar configuração customizada do nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar build estático do stage anterior
COPY --from=builder /app/dist /usr/share/nginx/html

# Criar usuário não-root (nginx já roda como nginx user por padrão)
# Ajustar permissões
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chown -R nginx:nginx /var/cache/nginx && \
    chown -R nginx:nginx /var/log/nginx && \
    chown -R nginx:nginx /etc/nginx/conf.d && \
    touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid

# Mudar para usuário nginx
USER nginx

# Expor porta
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Entry point
CMD ["nginx", "-g", "daemon off;"]