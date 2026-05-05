import { useNavigate } from 'react-router-dom'
import { useBreadcrumbs } from '../hooks/useBreadcrumbs'
import type { LabelMap, DynamicResolver } from '../hooks/useBreadcrumbs'
import './Breadcrumb.css'

interface BreadcrumbProps {
  labels: LabelMap
  resolve?: DynamicResolver
}

export function Breadcrumb({ labels, resolve }: BreadcrumbProps) {
  const navigate = useNavigate()
  const entries = useBreadcrumbs(labels, resolve)

  if (entries.length === 0) return null

  return (
    <nav className="breadcrumb">
      {entries.map((entry, i) => (
        <span key={entry.path}>
          {i > 0 && <span className="breadcrumb-separator">›</span>}
          {entry.isLast
            ? <span className="breadcrumb-current">{entry.label}</span>
            : <button className="breadcrumb-link" onClick={() => navigate(entry.path)}>{entry.label}</button>
          }
        </span>
      ))}
    </nav>
  )
}
