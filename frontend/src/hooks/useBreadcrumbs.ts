import { useLocation } from 'react-router-dom'

export interface BreadcrumbEntry {
  label: string
  path: string
  isLast: boolean
}

export type LabelMap = Record<string, string>
export type DynamicResolver = (segment: string, fullPath: string) => string | null

export function useBreadcrumbs(labels: LabelMap, resolve?: DynamicResolver): BreadcrumbEntry[] {
  const { pathname } = useLocation()

  if (pathname === '/') return []

  const segments = pathname.split('/')
  const entries: BreadcrumbEntry[] = []

  if (labels['/']) {
    entries.push({ label: labels['/'], path: '/', isLast: false })
  }

  let currentPath = ''
  for (let i = 1; i < segments.length; i++) {
    currentPath += '/' + segments[i]
    const label = labels[currentPath] ?? resolve?.(segments[i], currentPath) ?? null
    if (label) {
      entries.push({ label, path: currentPath, isLast: false })
    }
  }

  if (entries.length > 0) {
    entries[entries.length - 1] = { ...entries[entries.length - 1], isLast: true }
  }

  return entries
}
